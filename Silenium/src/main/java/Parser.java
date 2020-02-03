import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser
{
  public String pars(String text)
  {
    String s = text.replaceAll("\\\\", "");
    String regul = "http.+.(jpeg|jpg|mp4|gif|JPG|webm|png)";
    return reg(s, regul);
  }

  private String reg(String text, String reg)
  {
    String rez = "";
    StringBuilder builder = new StringBuilder();

    Pattern pattern = Pattern.compile(reg);
    Matcher matcher = pattern.matcher(text);
    while (matcher.find())
    {
      builder.append(text.substring(matcher.start(), matcher.end()) + "\n");
    }
    rez = builder.toString();
    System.out.println(rez);

    return rez;
  }
}
