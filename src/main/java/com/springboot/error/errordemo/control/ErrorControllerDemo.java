package com.springboot.error.errordemo.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ErrorControllerDemo implements ErrorController {

    private ErrorAttributes errorAttributes;

    @Autowired
    public ErrorControllerDemo(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @Override
    public String getErrorPath() {
        return "";
    }

    /**
     *  当请求一个不存在的url，会forward到/error/404，这个方法会映射到
     */
    @RequestMapping(produces = "text/html",value = "/error/404")
    public String errorHtml404(HttpServletRequest request,
                                     HttpServletResponse response) {
        return "404"; //对应templates/404.html
    }

    /**
     * 后台java抛异常,会forward到/error/500，这个方法会映射到
     */
    @RequestMapping(produces = "text/html",value = "/error/500")
    public String errorHtml500(HttpServletRequest request,
                               HttpServletResponse response,ModelMap map) {
//        Enumeration<String> enums = request.getAttributeNames();
//        Map<String,Object> errorMap = new HashMap<>();
//        while(enums.hasMoreElements()){
//            String str = enums.nextElement();
//            //if(str.indexOf("javax.servlet")>-1){
//                errorMap.put(str,request.getAttribute(str));
//           // }
//        }
//        map.addAttribute("errorMap",errorMap);

        //includeStackTrace 总是false，这样找不到stack trace ，抛出异常的时候不知道是哪行出的异常。
        //Map<String, Object> model = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.TEXT_HTML));
        Map<String, Object> model1 = getErrorAttributes(request,true);
        map.addAttribute("errorMap",model1);
        return "500";
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request,
                                                   boolean includeStackTrace) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return this.errorAttributes.getErrorAttributes(requestAttributes,
                includeStackTrace);
    }

    protected boolean isIncludeStackTrace(HttpServletRequest request,
                                          MediaType produces) {
        ErrorProperties.IncludeStacktrace include = this.serverProperties.getError().getIncludeStacktrace();
        if (include == ErrorProperties.IncludeStacktrace.ALWAYS) {
            return true;
        }
        if (include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM) {
            return getTraceParameter(request);
        }
        return false;
    }

    private boolean getTraceParameter(HttpServletRequest request) {
        String parameter = request.getParameter("trace");
        if (parameter == null) {
            return false;
        }
        return !"false".equals(parameter.toLowerCase());
    }

    @Autowired
    private ServerProperties serverProperties;

}
