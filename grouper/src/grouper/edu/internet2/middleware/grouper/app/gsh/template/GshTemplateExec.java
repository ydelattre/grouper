package edu.internet2.middleware.grouper.app.gsh.template;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.tools.shell.Groovysh;
import org.codehaus.groovy.tools.shell.IO;
import org.codehaus.groovy.tools.shell.util.Logger;

import edu.internet2.middleware.grouper.Group;
import edu.internet2.middleware.grouper.GroupFinder;
import edu.internet2.middleware.grouper.GrouperSession;
import edu.internet2.middleware.grouper.Member;
import edu.internet2.middleware.grouper.MemberFinder;
import edu.internet2.middleware.grouper.Stem;
import edu.internet2.middleware.grouper.StemFinder;
import edu.internet2.middleware.grouper.SubjectFinder;
import edu.internet2.middleware.grouper.app.gsh.GrouperGroovysh;
import edu.internet2.middleware.grouper.cfg.GrouperConfig;
import edu.internet2.middleware.grouper.exception.GrouperSessionException;
import edu.internet2.middleware.grouper.misc.GrouperSessionHandler;
import edu.internet2.middleware.grouper.util.GrouperUtil;
import edu.internet2.middleware.subject.Subject;
import jline.TerminalFactory;

public class GshTemplateExec {
  
  private GshTemplateOwnerType gshTemplateOwnerType;
  
  private String ownerStemName;
  
  private String ownerGroupName;
  
  private String configId; // gsh template config to run
  
  private Subject currentUser; // user using the UI or the webservice
  
  private List<GshTemplateInput> gshTemplateInputs = new ArrayList<GshTemplateInput>();
  
  
  public GshTemplateExec assignGshTemplateOwnerType(GshTemplateOwnerType gshTemplateOwnerType) {
    this.gshTemplateOwnerType = gshTemplateOwnerType;
    return this;
  }
  
  public GshTemplateExec assignOwnerStemName(String ownerStemName) {
    this.ownerStemName = ownerStemName;
    return this;
  }
  
  public GshTemplateExec assignOwnerGroupName(String ownerGroupName) {
    this.ownerGroupName = ownerGroupName;
    return this;
  }
  
  public GshTemplateExec addGshTemplateInput(GshTemplateInput input) {
    gshTemplateInputs.add(input);
    return this;
  }
  
  public GshTemplateExec assignConfigId(String configId) {
    this.configId = configId;
    return this;
  }
  
  public GshTemplateExec assignCurrentUser(Subject currentUser) {
    this.currentUser = currentUser;
    return this;
  }

  public Subject getCurrentUser() {
    return currentUser;
  }

  public GshTemplateOwnerType getGshTemplateOwnerType() {
    return gshTemplateOwnerType;
  }

  
  public String getOwnerStemName() {
    return ownerStemName;
  }
  
  public String getOwnerGroupName() {
    return ownerGroupName;
  }

  
  public List<GshTemplateInput> getGshTemplateInputs() {
    return gshTemplateInputs;
  }
  
  
  public String getConfigId() {
    return configId;
  }

  
  private boolean validateEnabled(GshTemplateConfig templateConfig, GshTemplateExecOutput execOutput) {
    if (!templateConfig.isEnabled()) {
      execOutput.getGshTemplateOutput().addValidationLine("Template with configId "+configId+ " is not enabled.");
      return false;
    }
    return true;
  }
  
  private boolean validateOwnerType(GshTemplateConfig templateConfig, GshTemplateExecOutput execOutput) {
    if (this.gshTemplateOwnerType == GshTemplateOwnerType.stem) {
      String ownerStemString = this.getOwnerStemName();
      if (StringUtils.isBlank(ownerStemString)) {
        execOutput.getGshTemplateOutput().addValidationLine("For gshTemplateOwnerType 'stem'; ownerStemName cannot be blank.");
        return false;
      }
      
      
      Stem ownerStem = StemFinder.findByName(GrouperSession.staticGrouperSession(), ownerStemString, false);
      if (ownerStem == null) {
        execOutput.getGshTemplateOutput().addValidationLine("Could not find ownerStem for name: "+ownerStemString);
        return false;
      }
      
    } else if (this.gshTemplateOwnerType == GshTemplateOwnerType.group) {
      String ownerGroupString = this.getOwnerGroupName();
      if (StringUtils.isBlank(ownerGroupString)) {
        execOutput.getGshTemplateOutput().addValidationLine("For gshTemplateOwnerType 'group'; ownerGroupName cannot be blank.");
        return false;
      }
      
      
      Group ownerGroup = GroupFinder.findByName(GrouperSession.staticGrouperSession(), ownerGroupString, false);
      if (ownerGroup == null) {
        execOutput.getGshTemplateOutput().addValidationLine("Could not find ownerGroup for name: "+ownerGroupString);
        return false;
      }
      
    } else {
      execOutput.getGshTemplateOutput().addValidationLine("gshTemplateOwnerType is a required field");
      return false;
    }
    
    return true;
  }
  
  private boolean validateSecurityRunType(GshTemplateConfig templateConfig, GshTemplateExecOutput execOutput) {
    
    Member currentUserMember = MemberFinder.findBySubject(GrouperSession.staticGrouperSession(), currentUser, true);
    // validate current user
    if (templateConfig.getGshTemplateSecurityRunType() == GshTemplateSecurityRunType.specifiedGroup) {
      if (!templateConfig.getGroupThatCanRun().getMembers().contains(currentUserMember)) {
        execOutput.getGshTemplateOutput().addValidationLine("currentUser with subject id "+currentUser.getId() + " is not a member of "+templateConfig.getGroupThatCanRun().getName());
        return false;
      }
    } else if (templateConfig.getGshTemplateSecurityRunType() == GshTemplateSecurityRunType.wheel) {
      String wheelGroupName = GrouperConfig.retrieveConfig().propertyValueString("groups.wheel.group");
      if (StringUtils.isBlank(wheelGroupName)) {
        execOutput.getGshTemplateOutput().addValidationLine("securityRunType was chosen as wheelGroup but value of wheelGroup is not set.");
        return false;
      }
      
      Group wheelGroup = GroupFinder.findByName(GrouperSession.staticGrouperSession(), wheelGroupName, true);
      if (!wheelGroup.getMembers().contains(currentUserMember)) {
        execOutput.getGshTemplateOutput().addValidationLine("currentUser with subject id "+currentUser.getId() + " is not a member of wheelGroup "+wheelGroup.getName());
        return false;
      }
    } else if (templateConfig.getGshTemplateSecurityRunType() == GshTemplateSecurityRunType.privilegeOnObject) {
      
      if (this.gshTemplateOwnerType == GshTemplateOwnerType.stem) {
        String ownerStemString = this.getOwnerStemName();
        Stem ownerStem = StemFinder.findByName(GrouperSession.staticGrouperSession(), ownerStemString, true);
        
        GshTemplateRequireFolderPrivilege gshTemplateRequireFolderPrivilege = templateConfig.getGshTemplateRequireFolderPrivilege();
        
        if (!ownerStem.canHavePrivilege(currentUser, gshTemplateRequireFolderPrivilege.getPrivilege().getName(), true)) {
          execOutput.getGshTemplateOutput().addValidationLine("currentUser with subject id "+currentUser.getId() + " does not have "+gshTemplateRequireFolderPrivilege.getPrivilege().getName() + " on "+ ownerStemString);
          return false;
        }
        
      } else {
        String ownerGroupString = this.getOwnerGroupName();
        Group ownerGroup = GroupFinder.findByName(GrouperSession.staticGrouperSession(), ownerGroupString, true);
        
        GshTemplateRequireGroupPrivilege gshTemplateRequireGroupPrivilege = templateConfig.getGshTemplateRequireGroupPrivilege();
        if (!ownerGroup.canHavePrivilege(currentUser, gshTemplateRequireGroupPrivilege.getPrivilege().getName(), true)) {
          execOutput.getGshTemplateOutput().addValidationLine("currentUser with subject id "+currentUser.getId() + " does not have "+gshTemplateRequireGroupPrivilege.getPrivilege().getName() + " on "+ ownerGroupString);
          return false;
        }
        
      }
     
    }
    
    return true;
    
  }
  
  private boolean validateInputs(GshTemplateConfig templateConfig, GshTemplateExecOutput execOutput) {
    
    Map<String, GshTemplateInputConfig> inputConfigs = GrouperUtil.listToMap(templateConfig.getGshTemplateInputConfigs(), String.class, GshTemplateInputConfig.class, "name");
    
    for (GshTemplateInput gshTemplateInput: gshTemplateInputs) {
      
      {
        // make sure names passed in the input at runtime do exist in the config input names
        if (!inputConfigs.containsKey(gshTemplateInput.getName())) {
          execOutput.getGshTemplateOutput().addValidationLine(gshTemplateInput.getName() + " does not exist in the input names configured in the template. Valid names are "+ GrouperUtil.collectionToString(inputConfigs.keySet()));
          return false;
        }
      }
      
      GshTemplateInputConfig gshTemplateInputConfig = inputConfigs.get(gshTemplateInput.getName());
      String valueFromUser = gshTemplateInput.getValueString();
      
      {
        // required
        if (gshTemplateInputConfig.isRequired() && StringUtils.isBlank(valueFromUser)) {
          execOutput.getGshTemplateOutput().addValidationLine(gshTemplateInput.getName(), gshTemplateInput.getName() + " is a required input field.");
          return false;
        }
      }
      
      {
        // validate data type (string, int, boolean)
        GshTemplateInputType gshTemplateInputType = gshTemplateInputConfig.getGshTemplateInputType();
        boolean canBeConverted = gshTemplateInputType.canConvertToCorrectType(valueFromUser);
        if (!canBeConverted) {
          execOutput.getGshTemplateOutput().addValidationLine(gshTemplateInput.getName(), valueFromUser +" cannot be converted to "+gshTemplateInputType.name().toLowerCase());
          return false;
        }
      }
      
      {
        // validate the value provided by the user
        GshTemplateInputValidationType gshTemplateInputValidationType = gshTemplateInputConfig.getGshTemplateInputValidationType();
        if (gshTemplateInputValidationType == GshTemplateInputValidationType.regex) {
          boolean valuePasses = gshTemplateInputValidationType.doesValuePassValidation(gshTemplateInputConfig, valueFromUser, gshTemplateInputs);
          if (!valuePasses) {
            execOutput.getGshTemplateOutput().addValidationLine(gshTemplateInput.getName(), valueFromUser +" does not match regex: "+gshTemplateInputConfig.getValidationRegex());
            return false;
          }
        } else if (gshTemplateInputValidationType == GshTemplateInputValidationType.jexl) {
          boolean valuePasses = gshTemplateInputValidationType.doesValuePassValidation(gshTemplateInputConfig, valueFromUser, gshTemplateInputs);
          if (!valuePasses) {
            execOutput.getGshTemplateOutput().addValidationLine(gshTemplateInput.getName(), valueFromUser +" does not return true for jexl expression: "+gshTemplateInputConfig.getValidationJexl());
            return false;
          }
        } else if (gshTemplateInputValidationType == GshTemplateInputValidationType.builtin) {
          boolean valuePasses = gshTemplateInputValidationType.doesValuePassValidation(gshTemplateInputConfig, valueFromUser, gshTemplateInputs);
          if (!valuePasses) {
            execOutput.getGshTemplateOutput().addValidationLine(gshTemplateInput.getName(), valueFromUser +" does not match builtin validation: "+gshTemplateInputConfig.getValidationJexl());
            return false;
          }
        }
        
      }
      
    }
    
    //make sure all the required properties are actually there in the inputs provided by user
    Map<String, GshTemplateInput> gshTemplateUserInputs = GrouperUtil.listToMap(getGshTemplateInputs(), String.class, GshTemplateInput.class, "name");
    
    for (String inputNameFromConfig: inputConfigs.keySet()) {
      
      if (inputConfigs.get(inputNameFromConfig).isRequired() && !gshTemplateUserInputs.containsKey(inputNameFromConfig)) {
        execOutput.getGshTemplateOutput().addValidationLine(inputNameFromConfig +" is a required input and must exist in the input params.");
        return false;
      }
      
    }
    
    return true;
    
  }
  
  private boolean validate(GshTemplateConfig templateConfig, GshTemplateExecOutput execOutput) {
    
    boolean isValid = validateEnabled(templateConfig, execOutput);
    isValid = isValid && validateOwnerType(templateConfig, execOutput);
    isValid = isValid && validateSecurityRunType(templateConfig, execOutput);
    isValid = isValid && validateInputs(templateConfig, execOutput);
    return isValid;
    
  }
  
  // execute the gsh template
  public GshTemplateExecOutput execute() {
    
    GshTemplateExecOutput execOutput = new GshTemplateExecOutput();
    
    final GshTemplateOutput gshTemplateOutput = new GshTemplateOutput();
    
    execOutput.setGshTemplateOutput(gshTemplateOutput);
    
    final GshTemplateRuntime gshTemplateRuntime = new GshTemplateRuntime();
    
    if (StringUtils.isBlank(configId)) {
      execOutput.getGshTemplateOutput().addValidationLine("configId cannot be blank");
      return execOutput;
    }
    
    if (currentUser == null) {
      throw new RuntimeException("currentUser cannot be null");
    }
    
    GshTemplateConfig templateConfig = new GshTemplateConfig(configId);
    
    Subject grouperSessionSubject = (Subject) GrouperSession.internal_callbackRootGrouperSession(new GrouperSessionHandler() {
      
      @Override
      public Subject callback(GrouperSession grouperSession) throws GrouperSessionException {
        
        gshTemplateRuntime.setCurrentSubject(currentUser);
        templateConfig.populateConfiguration();
        if (!validate(templateConfig, execOutput)) {
          return null;
        }
        
        Subject grouperSessionSubject = null;
        
        if (templateConfig.getGshTemplateRunAsType() == GshTemplateRunAsType.GrouperSystem) {
          grouperSessionSubject = SubjectFinder.findRootSubject();
        } else if (templateConfig.getGshTemplateRunAsType() == GshTemplateRunAsType.currentUser) {
          grouperSessionSubject = currentUser;
        } else if (templateConfig.getGshTemplateRunAsType() == GshTemplateRunAsType.specifiedSubject) {
          
          String subjectId = templateConfig.getRunAsSpecifiedSubjectId();
          String sourceId = templateConfig.getRunAsSpecifiedSubjectSourceId();
          
          grouperSessionSubject = SubjectFinder.findByIdAndSource(subjectId, sourceId, true);
          
        }
        
        return grouperSessionSubject;
      }
    });
    
  
    if (execOutput.getGshTemplateOutput().getValidationLines().size() > 0) {
      execOutput.setValid(false);
      return execOutput;
    } else {
      execOutput.setValid(true);
    }
    
    StringBuilder scriptToRun = new StringBuilder();
    
    scriptToRun.append("import edu.internet2.middleware.grouper.app.gsh.template.*;");
    scriptToRun.append("\n");

    scriptToRun.append("import edu.internet2.middleware.grouper.util.*;");
    scriptToRun.append("\n");
    
    scriptToRun.append("GshTemplateOutput gsh_builtin_gshTemplateOutput = GshTemplateOutput.retrieveGshTemplateOutput(); ");
    scriptToRun.append("\n");
    
    scriptToRun.append("GshTemplateRuntime gsh_builtin_gshTemplateRuntime = GshTemplateRuntime.retrieveGshTemplateRuntime(); ");
    scriptToRun.append("\n");
    
    scriptToRun.append("GrouperSession gsh_builtin_grouperSession = gsh_builtin_gshTemplateRuntime.getGrouperSession();");
    scriptToRun.append("\n");
    
    scriptToRun.append("Subject gsh_builtin_subject = gsh_builtin_gshTemplateRuntime.getCurrentSubject();");
    scriptToRun.append("\n");
    
    scriptToRun.append("String gsh_builtin_subjectId = \""+ StringEscapeUtils.escapeJava(currentUser.getId()) + "\";");
    scriptToRun.append("\n");
    
    if (this.gshTemplateOwnerType == GshTemplateOwnerType.stem) {
      scriptToRun.append("String gsh_builtin_ownerStemName = \""+StringEscapeUtils.escapeJava(ownerStemName) + "\";");
      scriptToRun.append("\n");
    } else {
      scriptToRun.append("String gsh_builtin_ownerGroupName = \""+StringEscapeUtils.escapeJava(ownerGroupName) + "\";");
      scriptToRun.append("\n");
    }
    
    Map<String, GshTemplateInput> gshTemplateInputsMap = new HashMap<String, GshTemplateInput>();
    
    for (GshTemplateInput gshTemplateInput: gshTemplateInputs) {
      gshTemplateInputsMap.put(gshTemplateInput.getName(), gshTemplateInput);
    }
    
    for (GshTemplateInputConfig inputConfig: templateConfig.getGshTemplateInputConfigs()) {
      
      GshTemplateInput gshTemplateInput = gshTemplateInputsMap.get(inputConfig.getName());
      
      String valueFromUser = null;
      if (gshTemplateInput != null) {
        valueFromUser = gshTemplateInput.getValueString();
        if (inputConfig.isTrimWhitespace()) {
          valueFromUser = valueFromUser.trim();
        }
      }
      
      String gshVariable = inputConfig.getGshTemplateInputType().generateGshVariable(inputConfig, valueFromUser);
      
      scriptToRun.append(gshVariable);
      
    }
    
    scriptToRun.append(templateConfig.getGshTemplate());
    GrouperSession grouperSession = null;
    
    Calendar calendar = new GregorianCalendar();
    calendar.setTimeInMillis(System.currentTimeMillis());
    
    String tempFileParentDirPath = GrouperUtil.tmpDir(true) + "gshTemplates";
    //make sure the dir exists
    GrouperUtil.mkdirs(new File(tempFileParentDirPath));
    // $$timestamp$$ translates to current time in this format: yyyy_mm_dd_hh24_mi_ss
    String filename = configId + "_" + calendar.get(Calendar.YEAR) + "_"
        + StringUtils.leftPad(""+(calendar.get(Calendar.MONTH)+1), 2, '0') + "_" + StringUtils.leftPad(""+calendar.get(Calendar.DAY_OF_MONTH), 2, '0')
        + "_" + GrouperUtil.uniqueId();
    
    final File theFile = new File(tempFileParentDirPath + File.separator + filename);
    GrouperUtil.fileCreateNewFile(theFile);
    
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    PrintStream old = System.out;
    
    try {     
      GshTemplateOutput.assignThreadLocalGshTemplateOutput(gshTemplateOutput);
      GshTemplateRuntime.assignThreadLocalGshTemplateRuntime(gshTemplateRuntime);
      grouperSession = GrouperSession.start(grouperSessionSubject);
      gshTemplateRuntime.setGrouperSession(grouperSession);
      
      GrouperUtil.saveStringIntoFile(theFile, scriptToRun.toString());
      
      StringBuilder body = new StringBuilder();
      body.append(":load '" + GrouperUtil.fileFromResourceName("groovysh.profile").getAbsolutePath() + "'");
      body.append("\n" + ":gshFileLoad '" + theFile.getAbsolutePath() + "'");
      body.append("\n:exit");
      
      boolean exitOnError = true;
      
      org.codehaus.groovy.tools.shell.Main.setTerminalType(TerminalFactory.AUTO, false);
      IO io = new IO();
      CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
      Logger.io = io;
      compilerConfiguration.setParameters(false);

      final Groovysh shell = new GrouperGroovysh(io, compilerConfiguration, exitOnError);
      
      System.setOut(ps);
      
      int code = 0;
      try {
        code = shell.run(body.toString());
      } catch (GshTemplateReturnException e) {
        
      }
      if (code != 0) {
        execOutput.setSuccess(false);
      } else {
        if (gshTemplateOutput.isError()) {
          execOutput.setSuccess(false);
        } else {
          execOutput.setSuccess(true);
        }
      }
      
      System.out.flush();
     
      execOutput.setGshScriptOutput(baos.toString());
      
    } catch (RuntimeException e) {
      execOutput.setSuccess(false);
      execOutput.setException(e);
    } finally {
      GrouperSession.stopQuietly(grouperSession);
      GshTemplateOutput.removeThreadLocalGshTemplateOutput();
      GshTemplateRuntime.removeThreadLocalGshTemplateRuntime();
      
      GrouperUtil.deleteFile(theFile);
      System.setOut(old);
      GrouperUtil.closeQuietly(ps);
      GrouperUtil.closeQuietly(baos);
    }
    
    return execOutput;
  }

  
}