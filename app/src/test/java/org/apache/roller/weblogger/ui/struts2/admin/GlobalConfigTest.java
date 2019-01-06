package org.apache.roller.weblogger.ui.struts2.admin;

import org.apache.roller.weblogger.pojos.RuntimeConfigProperty;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.apache.roller.weblogger.ui.struts2.admin.GlobalConfig.updateProperties;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GlobalConfigTest {

    private Map<String, RuntimeConfigProperty> props = new HashMap<>();
    @Mock
    private Function<String, String> getParameter;
    @Mock
    private Supplier<String[]> getCommentPlugins;

    @Mock
    private RuntimeConfigProperty commentPluginsProperty;
    private RuntimeConfigProperty prop1 = new RuntimeConfigProperty("prop1Name", "prop1Value");
    private RuntimeConfigProperty prop2 = new RuntimeConfigProperty("prop2Name", "prop2Value");
    private RuntimeConfigProperty booleanProp = new RuntimeConfigProperty("booleanProp", "false");

    @Before
    public void setUp() throws Exception {
        when(getCommentPlugins.get()).thenReturn(new String[0]);
        props.put("users.comments.plugins", commentPluginsProperty);
        props.put(prop1.getName(), prop1);
        props.put(prop2.getName(), prop2);
        props.put(booleanProp.getName(), booleanProp);
    }

    @Test
    public void propsGetsUpdatedBasedOnWhatGetParameterReturns() {
        when(getParameter.apply("prop1Name")).thenReturn("prop1NewValue");

        updateProperties(props, getParameter, getCommentPlugins);

        assertThat(props.get("prop1Name").getValue(), is("prop1NewValue"));
    }

    @Test
    public void handlesBooleanOneAppropriately() {
        when(getParameter.apply("booleanProp")).thenReturn("True");

        updateProperties(props, getParameter, getCommentPlugins);

        assertThat(props.get("booleanProp").getValue(), is("true"));
    }
}
