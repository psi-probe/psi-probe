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

import com.googlecode.psiprobe.tools.BackwardsFileStream;
import com.googlecode.psiprobe.tools.BackwardsLineReader;
import java.io.File;
import java.util.LinkedList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

public class FollowController extends LogHandlerController  {

    private int maxLines;
    private int initialLines;

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

    protected ModelAndView handleLogFile(HttpServletRequest request, HttpServletResponse response, File file) throws Exception {

        ModelAndView mv = new ModelAndView(getViewName());
        LinkedList lines = new LinkedList();
        long lastKnownLength = ServletRequestUtils.getLongParameter(request, "lastKnownLength", 0);

            if (file.exists()) {
                long currentLength = file.length();
                long readSize = 0;

                if (currentLength < lastKnownLength) {
                    //
                    // file length got reset
                    //
                    lastKnownLength = 0;
                    lines.add(" ------------- THE FILE HAS BEEN TRUNCATED --------------");
                }

                BackwardsFileStream bfs = new BackwardsFileStream(file, currentLength);
                try {
                    BackwardsLineReader br = new BackwardsLineReader(bfs);
                    String s;
                    while (readSize < currentLength - lastKnownLength && (s = br.readLine()) != null) {
                        if (lines.size() >= maxLines) {
                            break;
                        }
                        if (!s.equals("")){
                            lines.addFirst(s);
                            readSize += s.length();
                        } else {
                            readSize++;
                        }
                        if (lastKnownLength == 0 && lines.size() >= initialLines) {
                            break;
                        }
                    }

                    if (readSize > currentLength - lastKnownLength) {
                        lines.removeFirst();
                    }

                    mv.addObject("lines", lines);
                } finally {
                    bfs.close();
                }
            }

        return mv;
    }
}
