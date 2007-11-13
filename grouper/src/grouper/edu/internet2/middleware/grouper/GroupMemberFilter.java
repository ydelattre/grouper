/*
  Copyright (C) 2004-2007 University Corporation for Advanced Internet Development, Inc.
  Copyright (C) 2004-2007 The University Of Chicago

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

package edu.internet2.middleware.grouper;

import  edu.internet2.middleware.subject.Subject;
import  java.util.Set;
import  java.util.LinkedHashSet;


/** 
 * Query for groups with a member.
 * <p/>
 * @author  Shilen Patel
 */
public class GroupMemberFilter extends BaseQueryFilter {

  // Private Instance Variables
  private Subject subj;      
  private Stem ns;


  // Constructors

  /**
   * {@link QueryFilter} that returns groups a member belongs to.
   * specified date. 
   * <p/>
   * @param   subj  Find groups that this subject is a member of.
   * @param   ns    Restrict results to within this stem.
   */
  public GroupMemberFilter(Subject subj, Stem ns) {
    this.subj = subj;
    this.ns = ns;
  } // public GroupMemberFilter(subj, ns)


  // Public Instance Methods

  public Set getResults(GrouperSession s) 
    throws QueryException
  {
    GrouperSession.validate(s);

    Member member = null;
    try {
      member = MemberFinder.findBySubject(s, subj);
    }
    catch (MemberNotFoundException e) {
      return new LinkedHashSet();
    }

    Set candidates  = PrivilegeHelper.canViewGroups(
      s, member.getGroups()
    );
    Set results     = this.filterByScope(this.ns, candidates);
    return results;
  } // public Set getResults(s)

}

