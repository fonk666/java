package nf.co.fonk;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.MockSettings;
import org.mockito.Mockito;

import com.sun.faces.config.ConfigManager;
import com.sun.faces.config.InitFacesContext;
import com.sun.faces.lifecycle.LifecycleImpl;
import com.sun.faces.util.FacesLogger;

public class JSFAlone {
	
	public static void main(String[] args) {
		render("/" + args[0], System.out);
	}

	
	public static void render(String path, OutputStream os) {
		MockSettings mockitoSettings = Mockito.withSettings();
		
		ServletContextMock sc = Mockito.mock(ServletContextMock.class, mockitoSettings);
		sc.init();
		
		InitFacesContext initFacesContext = new InitFacesContext(sc);

		ConfigManager configManager = ConfigManager.getInstance();
		configManager.initialize(sc);
		FacesContextFactory factory = (FacesContextFactory) FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);

		HttpServletRequest request = Mockito.mock(HttpServletRequest.class, mockitoSettings);
		HttpServletResponse response = Mockito.mock(HttpServletResponse.class, mockitoSettings);

		Mockito.when(request.getServletPath()).thenReturn(path);
		Enumeration<Locale> locales = new Vector<>(
				Arrays.asList(new Locale("es_ES"), new Locale("es"), new Locale("en_EN"), new Locale("en"))
			).elements();
		Mockito.when(request.getLocales()).thenReturn(locales);

		Enumeration<String> acceptHeaders = new Vector<>(Collections.singleton("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")).elements();
		Mockito.when(request.getHeaders("Accept")).thenReturn(acceptHeaders);

		PrintWriter printWriter = new PrintWriter(os);
		try {
			Mockito.when(response.getWriter()).thenReturn(printWriter);
		} catch (IOException e) {
			//Imposible
		}
		
		LifecycleImpl lifecycleImpl = new LifecycleImpl(initFacesContext);
		FacesContext facesContext = factory.getFacesContext(sc, request, response, lifecycleImpl);
		
		LiteralValueExpression ve = Mockito.mock(LiteralValueExpression.class, mockitoSettings);
		ve.setValue(new Bean1());
		facesContext.getELContext().getVariableMapper().setVariable("value", ve);
		
		lifecycleImpl.execute(facesContext);
		lifecycleImpl.render(facesContext);
		
		printWriter.close();
	}	
	
	
	private abstract static class LiteralValueExpression extends ValueExpression {
	    private static final long serialVersionUID = 1L;
	    private Object value;

	    public final void setValue(Object value) {
	        this.value = value;
	    }
	    
	    @Override final public Object getValue(ELContext context) {
	        return value;
	    }
	}
	
	private abstract class ServletContextMock implements ServletContext {
	    ConcurrentMap<String, Object> attributes;
	    HashMap<String, String> initParameters;
	    
		@Override final public Object getAttribute(String arg0) {
			return attributes.get(arg0);
		}
		final public void init() {
			attributes = new ConcurrentHashMap<>();
			initParameters = new HashMap<>();
		}
		@Override final public void setAttribute(String arg0, Object arg1) {
			attributes.put(arg0, arg1);
		}
		@Override final public URL getResource(String arg0) throws MalformedURLException {
			return this.getClass().getResource(arg0);
		}
		@Override final public String getInitParameter(String arg0) {
			return initParameters.get(arg0);
		}
		@Override final public Enumeration<String> getInitParameterNames() {
			return new Vector<>(initParameters.keySet()).elements();
		}
	}
	
}




