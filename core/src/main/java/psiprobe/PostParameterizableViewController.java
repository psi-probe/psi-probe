package psiprobe;

import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 * The Class PostParameterizableViewController.
 */
public class PostParameterizableViewController extends ParameterizableViewController {

  /**
   * Instantiates a new post parameterizable view controller. As of spring 4.3.x, the
   * ParameterizableViewController no longer allows POST but only GET/HEAD. This patch
   * restores POST functionality.
   */
  public PostParameterizableViewController() {
    super();
    setSupportedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.HEAD.name());
  }

}
