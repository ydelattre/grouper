/**
 * Copyright 2014 Internet2
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.internet2.middleware.grouper.ldap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

import edu.internet2.middleware.grouper.cfg.GrouperConfig;
import edu.internet2.middleware.grouper.ldap.ldaptive.LdaptiveSessionImpl;
import edu.internet2.middleware.grouper.util.GrouperUtil;

/**
 * @author shilen
 */
public class LdapSessionUtils {

  /** logger */
  private static final Log LOG = GrouperUtil.getLog(LdapSessionUtils.class);

  private static LdapSession ldapSession = null;

  private static boolean loggedErrorNotLdaptive = false;
  
  /**
   *
   * @return the external subject storable
   */
  public static LdapSession ldapSession() {
    if (ldapSession == null) {
      synchronized (LdapSessionUtils.class) {
        if (ldapSession == null) {
          String className = GrouperConfig.retrieveConfig().propertyValueString("ldap.implementation.className");
          
          if (!StringUtils.equals(className, LdaptiveSessionImpl.class.getName()) && !loggedErrorNotLdaptive) {
            LOG.error("ldap.implementation.className cannot be anything but " + LdaptiveSessionImpl.class.getName());
            loggedErrorNotLdaptive = true;
          }
          
          ldapSession = new LdaptiveSessionImpl(); 
        }
      }
    }
    
    return ldapSession;
  }
}
