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
package org.jstripe.tomcat.probe.controllers.logs;

import org.jstripe.tomcat.probe.model.FollowedFile;
import org.jstripe.tomcat.probe.tools.BackwardsFileStream;
import org.jstripe.tomcat.probe.tools.BackwardsLineReader;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

public class FollowController extends ParameterizableViewController  {

    private int maxLines;
    private int initialLines;
    private String fileAttributeName;

    public int getMaxLines() {
        return maxLines;
    }

    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
    }

    public int getInitialLines() {
        return initialLines;
    }

    public void setInitialLines(int initialLines) {
        this.initialLines = initialLines;
    }

    public String getFileAttributeName() {
        return fileAttributeName;
    }

    public void setFileAttributeName(String fileAttributeName) {
        this.fileAttributeName = fileAttributeName;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        FollowedFile ff = (FollowedFile) request.getSession(true).getAttribute("followed_file");

        if (ff != null) {
            File f = new File(ff.getFileName());

            if (f.exists()) {
                long currentLength = f.length();
                long readSize = 0;
                int listSize = ff.getLines().size();

                if (currentLength < ff.getLastKnowLength()) {
                    //
                    // file length got reset
                    //
                    ff.setLastKnowLength(0);
                    ff.getLines().add(listSize, " ------------- THE FILE HAS BEEN TRUNCATED --------------");
                }

                BackwardsFileStream bfs = new BackwardsFileStream(f, currentLength);
                try {
                    BackwardsLineReader br = new BackwardsLineReader(bfs);
                    String s;
                    while (readSize < currentLength - ff.getLastKnowLength() && (s = br.readLine()) != null) {
                        if (ff.getLines().size() >= maxLines) {
                            if (listSize > 0) {
                                ff.getLines().remove(0);
                                listSize--;
                            } else {
                                break;
                            }
                        }
                        if (!s.equals("")){
                            ff.getLines().add(listSize, s);
                            readSize += s.length();
                        } else {
                            readSize++;
                        }
                        if (ff.getLastKnowLength() == 0 && ff.getLines().size() >= initialLines) {
                            break;
                        }
                    }

                    if (readSize > currentLength - ff.getLastKnowLength() && listSize > 0) {
                        ff.getLines().remove(listSize-1);
                    }

                    ff.setLastKnowLength(currentLength);
                } finally {
                    bfs.close();
                }
            } else {
                ff.getLines().clear();
            }
            request.getSession(true).setAttribute(fileAttributeName, ff);
        }

        return new ModelAndView(getViewName());
    }
}
