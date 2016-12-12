/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.controllers.apps;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The Class AllAppStatsController.
 */
public class AllAppStatsController extends ParameterizableViewController {

  /** The collection period. */
  private long collectionPeriod;

  /**
   * Gets the collection period.
   *
   * @return the collection period
   */
  public long getCollectionPeriod() {
    return collectionPeriod;
  }

  /**
   * Sets the collection period.
   *
   * @param collectionPeriod the new collection period
   */
  public void setCollectionPeriod(long collectionPeriod) {
    this.collectionPeriod = collectionPeriod;
  }

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    return super.handleRequestInternal(request, response)
        .addObject("collectionPeriod", getCollectionPeriod());
  }

}
