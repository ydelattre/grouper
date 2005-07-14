/*--
$Id: PrivilegeImpl.java,v 1.2 2005-07-14 00:13:34 acohen Exp $
$Date: 2005-07-14 00:13:34 $

Copyright 2004 Internet2 and Stanford University.  All Rights Reserved.
Licensed under the Signet License, Version 1,
see doc/license.txt in this distribution.
*/
package edu.internet2.middleware.signet;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.collections.set.UnmodifiableSet;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author Andy Cohen
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PrivilegeImpl implements Privilege
{
  Permission permission;
  Set        limitValues;
  
  /**
   * Hibernate requires the presence of a default constructor.
   */
  public PrivilegeImpl()
  {
    super();
  }
  
  private PrivilegeImpl(Permission permission, Set limitValues)
  {
    this.permission = permission;
    this.limitValues = limitValues;
  }
  
  static Set getPrivileges(Assignment assignment)
  {
    Set privileges = new HashSet();
    
    Permission[] assignmentPermissions
      = assignment.getFunction().getPermissionsArray();
    
    Set assignmentLimitValues = assignment.getLimitValues();
    
    for (int i = 0; i < assignmentPermissions.length; i++)
    {
      Permission permission = assignmentPermissions[i];
      
      Limit[] permissionLimits = permission.getLimitsArray();      
      Set permissionLimitValues
        = filterLimitValues(permissionLimits, assignmentLimitValues);
      
      Privilege privilege
        = new PrivilegeImpl(permission, permissionLimitValues);
      
      privileges.add(privilege);
    }
    
    return privileges;
  }
  
  static Set filterLimitValues
    (Limit  limit,
     Set    limitValues)
  {
    Set filteredLimitValues = new HashSet();
    
    Iterator limitValuesIterator = limitValues.iterator();
    while (limitValuesIterator.hasNext())
    {
      LimitValue candidate = (LimitValue)(limitValuesIterator.next());
      
      if (candidate.getLimit().equals(limit))
      {
        filteredLimitValues.add(candidate);
      }
    }
    
    return filteredLimitValues;
  }
  
  static Set filterLimitValues
    (Limit[]  limits,
     Set      limitValues)
  {
     Set filteredLimitValues = new HashSet();
     
     for (int i = 0; i < limits.length; i++)
     {
       Limit limit = limits[i];
       filteredLimitValues.addAll(filterLimitValues(limit, limitValues));
     }
     
     return filteredLimitValues;
  }

  /* (non-Javadoc)
   * @see edu.internet2.middleware.signet.Privilege#getPermission()
   */
  public Permission getPermission()
  {
    return this.permission;
  }

  /* (non-Javadoc)
   * @see edu.internet2.middleware.signet.Privilege#getLimitValues()
   */
  public Set getLimitValues()
  {
    return  UnmodifiableSet.decorate(this.limitValues);
  }

  public boolean equals(Object obj)
  {
    if ( !(obj instanceof PrivilegeImpl) )
    {
      return false;
    }
    
    PrivilegeImpl rhs = (PrivilegeImpl) obj;
    return new EqualsBuilder()
      .append(this.getPermission(), rhs.getPermission())
      .append(this.getLimitValues(), rhs.getLimitValues())
      .isEquals();
  }
  
  public int hashCode()
  {
    // you pick a hard-coded, randomly chosen, non-zero, odd number
    // ideally different for each class
    return new HashCodeBuilder(17, 37)
      .append(this.permission)
      .append(this.limitValues)
      .toHashCode();
  }
}
