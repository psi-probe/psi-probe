/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */

package psiprobe.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data model class used by session search feature of application session screen.
 * 
 * @author Andy Shapoval
 * @author Vlad Ilyushchenko
 */
public class SessionSearchInfo implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The Constant Logger. */
  private static final Logger logger = LoggerFactory.getLogger(SessionSearchInfo.class);

  /** The Constant SESS_ATTR_NAME. */
  public static final String SESS_ATTR_NAME = "sessionSearchInfo";
  
  /** The Constant ACTION_NONE. */
  public static final String ACTION_NONE = "none";
  
  /** The Constant ACTION_APPLY. */
  public static final String ACTION_APPLY = "apply";
  
  /** The Constant ACTION_CLEAR. */
  public static final String ACTION_CLEAR = "clear";

  /** The search action. */
  private String searchAction = ACTION_NONE;
  
  /** The apply. */
  private boolean apply;
  
  /** The clear. */
  private boolean clear;
  
  /** The session id. */
  private String sessionId;
  
  /** The session id pattern. */
  private Pattern sessionIdPattern;
  
  /** The session id msg. */
  private String sessionIdMsg;
  
  /** The attr name. */
  private String attrName;
  
  /** The attr name patterns. */
  private final List<Pattern> attrNamePatterns = new ArrayList<>();
  
  /** The attr name msgs. */
  private final List<String> attrNameMsgs = new ArrayList<>();
  
  /** The age from. */
  private String ageFrom;
  
  /** The age from sec. */
  private Integer ageFromSec;
  
  /** The age to. */
  private String ageTo;
  
  /** The age to sec. */
  private Integer ageToSec;
  
  /** The idle time from. */
  private String idleTimeFrom;
  
  /** The idle time from sec. */
  private Integer idleTimeFromSec;
  
  /** The idle time to. */
  private String idleTimeTo;
  
  /** The idle time to sec. */
  private Integer idleTimeToSec;
  
  /** The last ip. */
  private String lastIp;
  
  /** The info message. */
  private String infoMessage;
  
  /** The error messages. */
  private final List<String> errorMessages = new ArrayList<>();

  /**
   * Checks if is empty.
   *
   * @return true, if is empty
   */
  public boolean isEmpty() {
    return this.sessionId == null && this.attrName == null && this.ageFrom == null && this.ageTo == null
        && this.idleTimeFrom == null && this.idleTimeTo == null && this.lastIp == null;
  }

  /**
   * Checks if is session id valid.
   *
   * @return true, if is session id valid
   */
  public boolean isSessionIdValid() {
    return this.sessionId == null || this.sessionIdPattern != null;
  }

  /**
   * Checks if is attr name valid.
   *
   * @return true, if is attr name valid
   */
  public boolean isAttrNameValid() {
    return this.attrName == null || !this.attrNamePatterns.isEmpty();
  }

  /**
   * Checks if is age from valid.
   *
   * @return true, if is age from valid
   */
  public boolean isAgeFromValid() {
    return this.ageFrom == null || this.ageFromSec != null;
  }

  /**
   * Checks if is age to valid.
   *
   * @return true, if is age to valid
   */
  public boolean isAgeToValid() {
    return this.ageTo == null || this.ageToSec != null;
  }

  /**
   * Checks if is idle time from valid.
   *
   * @return true, if is idle time from valid
   */
  public boolean isIdleTimeFromValid() {
    return this.idleTimeFrom == null || this.idleTimeFromSec != null;
  }

  /**
   * Checks if is idle time to valid.
   *
   * @return true, if is idle time to valid
   */
  public boolean isIdleTimeToValid() {
    return this.idleTimeTo == null || this.idleTimeToSec != null;
  }

  /**
   * Checks if is valid.
   *
   * @return true, if is valid
   */
  public boolean isValid() {
    return isSessionIdValid() && isAttrNameValid() && isAgeFromValid() && isAgeToValid()
        && isIdleTimeFromValid() && isIdleTimeToValid();
  }

  /**
   * Checks if is use search.
   *
   * @return true, if is use search
   */
  public boolean isUseSearch() {
    return isApply() && !isEmpty() && isValid();
  }

  /**
   * Checks if is use session id.
   *
   * @return true, if is use session id
   */
  public boolean isUseSessionId() {
    return this.sessionIdPattern != null;
  }

  /**
   * Checks if is use attr name.
   *
   * @return true, if is use attr name
   */
  public boolean isUseAttrName() {
    return !this.attrNamePatterns.isEmpty();
  }

  /**
   * Checks if is use attr.
   *
   * @return true, if is use attr
   */
  public boolean isUseAttr() {
    return isUseSearch() && isUseAttrName();
  }

  /**
   * Checks if is use age from.
   *
   * @return true, if is use age from
   */
  public boolean isUseAgeFrom() {
    return this.ageFromSec != null;
  }

  /**
   * Checks if is use age to.
   *
   * @return true, if is use age to
   */
  public boolean isUseAgeTo() {
    return this.ageToSec != null;
  }

  /**
   * Checks if is use idle time from.
   *
   * @return true, if is use idle time from
   */
  public boolean isUseIdleTimeFrom() {
    return this.idleTimeFromSec != null;
  }

  /**
   * Checks if is use idle time to.
   *
   * @return true, if is use idle time to
   */
  public boolean isUseIdleTimeTo() {
    return this.idleTimeToSec != null;
  }

  /**
   * Checks if is use last ip.
   *
   * @return true, if is use last ip
   */
  public boolean isUseLastIp() {
    return this.lastIp != null;
  }

  /**
   * Gets the search action.
   *
   * @return the search action
   */
  public String getSearchAction() {
    return this.searchAction;
  }

  /**
   * Sets the search action.
   *
   * @param searchAction the new search action
   */
  public void setSearchAction(String searchAction) {
    this.searchAction = searchAction;
    if (searchAction == null) {
      this.apply = false;
      this.clear = false;
    } else {
      this.apply = searchAction.equals(ACTION_APPLY);
      this.clear = searchAction.equals(ACTION_CLEAR);
    }
  }

  /**
   * Checks if is apply.
   *
   * @return true, if is apply
   */
  public boolean isApply() {
    return this.apply;
  }

  /**
   * Checks if is clear.
   *
   * @return true, if is clear
   */
  public boolean isClear() {
    return this.clear;
  }

  /**
   * Gets the session id.
   *
   * @return the session id
   */
  public String getSessionId() {
    return this.sessionId;
  }

  /**
   * Sets the session id.
   *
   * @param sessionId the new session id
   */
  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
    this.sessionIdPattern = null;
    this.sessionIdMsg = null;

    if (sessionId != null) {
      try {
        this.sessionIdPattern = Pattern.compile(sessionId);
      } catch (PatternSyntaxException e) {
        logger.trace("", e);
        this.sessionIdMsg = e.getDescription();
      }
    }
  }

  /**
   * Gets the session id pattern.
   *
   * @return the session id pattern
   */
  public Pattern getSessionIdPattern() {
    return this.sessionIdPattern;
  }

  /**
   * Gets the session id msg.
   *
   * @return the session id msg
   */
  public String getSessionIdMsg() {
    return this.sessionIdMsg;
  }

  /**
   * Gets the attr name.
   *
   * @return the attr name
   */
  public String getAttrName() {
    return this.attrName;
  }

  /**
   * Sets the attr name.
   *
   * @param attrName the new attr name
   */
  public void setAttrName(String attrName) {
    this.attrName = attrName;
    this.attrNamePatterns.clear();
    this.attrNameMsgs.clear();

    if (attrName != null) {
      String[] attrNames = attrName.split(",");
      if (attrNames.length == 0 && !attrName.equals("")) {
        this.attrNameMsgs.add("");
      } else {
        for (String regex : attrNames) {
          try {
            this.attrNamePatterns.add(Pattern.compile(regex));
          } catch (PatternSyntaxException e) {
            logger.trace("", e);
            this.attrNameMsgs.add(e.getDescription());
          }
        }
      }
    }
  }

  /**
   * Gets the attr name patterns.
   *
   * @return the attr name patterns
   */
  public List<Pattern> getAttrNamePatterns() {
    return this.attrNamePatterns;
  }

  /**
   * Gets the attr name msgs.
   *
   * @return the attr name msgs
   */
  public List<String> getAttrNameMsgs() {
    return this.attrNameMsgs;
  }

  /**
   * Gets the age from.
   *
   * @return the age from
   */
  public String getAgeFrom() {
    return this.ageFrom;
  }

  /**
   * Sets the age from.
   *
   * @param ageFrom the new age from
   */
  public void setAgeFrom(String ageFrom) {
    this.ageFrom = ageFrom;
    this.ageFromSec = null;

    if (ageFrom != null) {
      try {
        this.ageFromSec = Integer.valueOf(ageFrom);
      } catch (NumberFormatException e) {
        logger.trace("", e);
      }
    }
  }

  /**
   * Gets the age from sec.
   *
   * @return the age from sec
   */
  public Integer getAgeFromSec() {
    return this.ageFromSec;
  }

  /**
   * Gets the age to.
   *
   * @return the age to
   */
  public String getAgeTo() {
    return this.ageTo;
  }

  /**
   * Sets the age to.
   *
   * @param ageTo the new age to
   */
  public void setAgeTo(String ageTo) {
    this.ageTo = ageTo;
    this.ageToSec = null;

    if (ageTo != null) {
      try {
        this.ageToSec = Integer.valueOf(ageTo);
      } catch (NumberFormatException e) {
        logger.trace("", e);
      }
    }
  }

  /**
   * Gets the age to sec.
   *
   * @return the age to sec
   */
  public Integer getAgeToSec() {
    return this.ageToSec;
  }

  /**
   * Gets the idle time from.
   *
   * @return the idle time from
   */
  public String getIdleTimeFrom() {
    return this.idleTimeFrom;
  }

  /**
   * Sets the idle time from.
   *
   * @param idleTimeFrom the new idle time from
   */
  public void setIdleTimeFrom(String idleTimeFrom) {
    this.idleTimeFrom = idleTimeFrom;
    this.idleTimeFromSec = null;

    if (idleTimeFrom != null) {
      try {
        this.idleTimeFromSec = Integer.valueOf(idleTimeFrom);
      } catch (NumberFormatException e) {
        logger.trace("", e);
      }
    }
  }

  /**
   * Gets the idle time from sec.
   *
   * @return the idle time from sec
   */
  public Integer getIdleTimeFromSec() {
    return this.idleTimeFromSec;
  }

  /**
   * Gets the idle time to.
   *
   * @return the idle time to
   */
  public String getIdleTimeTo() {
    return this.idleTimeTo;
  }

  /**
   * Sets the idle time to.
   *
   * @param idleTimeTo the new idle time to
   */
  public void setIdleTimeTo(String idleTimeTo) {
    this.idleTimeTo = idleTimeTo;
    this.idleTimeToSec = null;

    if (idleTimeTo != null) {
      try {
        this.idleTimeToSec = Integer.valueOf(idleTimeTo);
      } catch (NumberFormatException e) {
        logger.trace("", e);
      }
    }
  }

  /**
   * Gets the idle time to sec.
   *
   * @return the idle time to sec
   */
  public Integer getIdleTimeToSec() {
    return this.idleTimeToSec;
  }

  /**
   * Gets the last ip.
   *
   * @return the last ip
   */
  public String getLastIp() {
    return this.lastIp;
  }

  /**
   * Sets the last ip.
   *
   * @param lastIp the new last ip
   */
  public void setLastIp(String lastIp) {
    this.lastIp = lastIp;
  }

  /**
   * Gets the info message.
   *
   * @return the info message
   */
  public String getInfoMessage() {
    return this.infoMessage;
  }

  /**
   * Sets the info message.
   *
   * @param infoMessage the new info message
   */
  public void setInfoMessage(String infoMessage) {
    this.infoMessage = infoMessage;
  }

  /**
   * Gets the error messages.
   *
   * @return the error messages
   */
  public List<String> getErrorMessages() {
    return this.errorMessages;
  }

  /**
   * Adds the error message.
   *
   * @param msg the msg
   */
  public void addErrorMessage(String msg) {
    this.errorMessages.add(msg);
  }

}
