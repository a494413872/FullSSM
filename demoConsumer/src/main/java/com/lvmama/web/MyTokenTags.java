

package com.lvmama.web;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.lvmama.scenic.comm.web.TokenHandler;
import org.apache.log4j.Logger;

public class MyTokenTags extends TagSupport {
    private static final long serialVersionUID = -171194686952304210L;
    private static final Logger logger = Logger.getLogger(MyTokenTags.class);

    public MyTokenTags() {
    }

    public int doStartTag() throws JspException {
        String uuid = TokenHandler.getGUID(this.pageContext.getRequest(), this.pageContext.getResponse());
        logger.info("uuid = " + uuid);
        String html = "<input type='text' name='tokenName' readonly='readonly' value=" + uuid + ">";

        try {
            this.pageContext.getOut().print(html);
        } catch (IOException var4) {
            logger.error("Init tokenName error", var4);
        }

        return 1;
    }
}
