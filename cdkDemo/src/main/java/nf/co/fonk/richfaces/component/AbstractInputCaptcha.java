package nf.co.fonk.richfaces.component;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import javax.faces.component.UIInput;
import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64OutputStream;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

@JsfComponent(
        generate = "nf.co.fonk.richfaces.component.html.HtmlInputCaptcha",
        type = AbstractInputCaptcha.COMPONENT_TYPE,
        family = AbstractInputCaptcha.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = "nf.co.fonk.richfaces.component.InputCaptchaRenderer"),
        tag = @Tag(name = "inputCaptcha"))
public class AbstractInputCaptcha extends UIInput {
    public static final String COMPONENT_TYPE = "nf.co.fonk.richfaces.component.InputCaptcha";
    public static final String COMPONENT_FAMILY = "javax.faces.Input";

    @Attribute(hidden = true)
    public String getImageData() throws IOException{
		DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
		defaultKaptcha.setConfig(new Config(new Properties()));
		String createText = defaultKaptcha.createText();
		BufferedImage bufferedImage = defaultKaptcha.createImage(createText);
		

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		OutputStream b64 = new Base64OutputStream(os,true, Integer.MAX_VALUE, null);
		ImageIO.write(bufferedImage, "png", b64);
		String result = os.toString("UTF-8");
		return result;
    }
}
