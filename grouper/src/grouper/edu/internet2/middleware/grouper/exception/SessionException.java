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

package edu.internet2.middleware.grouper.exception;

/**
 * Exception thrown when there is a problem with a Grouper Session.
 * <p/>
 * @author  blair christensen.
 * @version $Id: SessionException.java,v 1.2 2009-03-15 06:37:23 mchyzer Exp $
 */
public class SessionException extends RuntimeException {
  private static final long serialVersionUID = -8563328666757581943L;
  public SessionException() { 
    super(); 
  }
  public SessionException(String msg) { 
    super(msg); 
  }
  public SessionException(String msg, Throwable cause) { 
    super(msg, cause); 
  }
  public SessionException(Throwable cause) { 
    super(cause); 
  }
}

