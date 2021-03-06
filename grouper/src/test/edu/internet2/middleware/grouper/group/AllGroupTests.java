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
/*
 * @author mchyzer $Id: AllGroupTests.java,v 1.3 2009-11-05 06:10:51 mchyzer Exp $
 */
package edu.internet2.middleware.grouper.group;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 *
 */
public class AllGroupTests {

  /**
   * main
   * @param args
   */
  public static void main(String[] args) {
    TestRunner.run(suite());
  }

  /**
   * 
   * @return test
   */
  public static Test suite() {
    TestSuite suite = new TestSuite("Test for edu.internet2.middleware.grouper.group");
    //$JUnit-BEGIN$
    suite.addTestSuite(TestGroupFinder.class);
    suite.addTestSuite(TestGroup1.class);
    suite.addTestSuite(TestWrongFieldType.class);
    suite.addTestSuite(TestGAttr.class);
    suite.addTestSuite(Test_I_API_Group_deleteAttribute.class);
    suite.addTestSuite(TestGroupModifyAttributes.class);
    suite.addTestSuite(TestGroup.class);
    suite.addTestSuite(Test_I_API_Group_addCompositeMember.class);
    suite.addTestSuite(TestGroupAddDeleteMember.class);
    suite.addTestSuite(Test_api_Group.class);
    suite.addTestSuite(Test_Integration_HibernateGroupDAO_delete.class);
    suite.addTestSuite(GroupDataTest.class);
    suite.addTestSuite(Test_I_API_Group_delete.class);
    suite.addTestSuite(Test_I_API_Group_deleteMember.class);
    suite.addTestSuite(Test_api_GrouperAPI.class);
    suite.addTestSuite(TestGroupReadonlyViewonly.class);
    suite.addTestSuite(TestDisabledGroup.class);
    //$JUnit-END$
    return suite;
  }

}
