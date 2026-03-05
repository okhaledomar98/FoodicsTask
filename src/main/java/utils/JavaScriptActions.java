package utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

public final class JavaScriptActions {

    private JavaScriptActions() {
    }

    public static void scrollIntoViewCenter(WebElement element) {
        ((JavascriptExecutor) DriverFactory.getDriver())
                .executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }

    public static void click(WebElement element) {
        ((JavascriptExecutor) DriverFactory.getDriver())
                .executeScript("arguments[0].click();", element);
    }

    public static void dispatchInputChangeBlur(WebElement element) {
        ((JavascriptExecutor) DriverFactory.getDriver()).executeScript(
                "arguments[0].dispatchEvent(new Event('input', {bubbles:true}));" +
                        "arguments[0].dispatchEvent(new Event('change', {bubbles:true}));" +
                        "arguments[0].dispatchEvent(new Event('blur', {bubbles:true}));", element);
    }

    public static boolean clickFirstVisibleBySelectors(String... selectors) {
        Object clicked = ((JavascriptExecutor) DriverFactory.getDriver()).executeScript(
                "var sels = arguments;" +
                        "for (var i=0;i<sels.length;i++){" +
                        "  var nodes=document.querySelectorAll(sels[i]);" +
                        "  for (var j=0;j<nodes.length;j++){" +
                        "    var el=nodes[j]; var r=el.getBoundingClientRect();" +
                        "    if(r.width>0 && r.height>0){ el.click(); return true; }" +
                        "  }" +
                        "}" +
                        "return false;"
                , (Object[]) selectors);
        return Boolean.TRUE.equals(clicked);
    }

    public static boolean submitFirstMatchingForm(String cssSelector) {
        Object submitted = ((JavascriptExecutor) DriverFactory.getDriver()).executeScript(
                "var forms=document.querySelectorAll(arguments[0]);" +
                        "for (var i=0;i<forms.length;i++){" +
                        "  try{ forms[i].requestSubmit ? forms[i].requestSubmit() : forms[i].submit(); return true; }catch(e){}" +
                        "}" +
                        "return false;", cssSelector);
        return Boolean.TRUE.equals(submitted);
    }
}
