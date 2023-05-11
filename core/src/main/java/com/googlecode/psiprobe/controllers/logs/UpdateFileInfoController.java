/*
 * Licensed under the GPL License.  You may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
 * MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package com.googlecode.psiprobe.controllers.logs;

import java.io.File;
import java.sql.Timestamp;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class UpdateFileInfoController extends LogHandlerController {

    protected ModelAndView handleLogFile(HttpServletRequest request, HttpServletResponse response, File file) throws Exception {
        ModelAndView mv = new ModelAndView(getViewName());
        String fileName = file.getAbsolutePath();
        if (file.exists()) {
            mv.addObject("fileName", fileName)
                    .addObject("lastModified", new Timestamp(file.lastModified()))
                    .addObject("size", new Long(file.length()));
        } else {
            logger.debug("File " + fileName + " does not exist");
        }
        return mv;
    }

}
