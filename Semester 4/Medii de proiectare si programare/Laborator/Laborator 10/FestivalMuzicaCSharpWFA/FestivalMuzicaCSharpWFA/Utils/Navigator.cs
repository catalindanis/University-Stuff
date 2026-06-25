// ...existing code...

namespace FestivalMuzicaCSharpWFA.Utils;

public static class Navigator
{
    private static Form mainForm;

    public static void SetMainForm(Form form)
    {
        mainForm = form;
    }

    public static Form NavigateTo(Form form, string title)
    {
        return NavigateTo(form, title, mainForm, null);
    }

    public static Form NavigateTo(Form form, string title, Dictionary<string, object> props)
    {
        return NavigateTo(form, title, mainForm, props);
    }

    public static Form NavigateTo(Form form, string title, bool newForm)
    {
        if (!newForm)
        {
            NavigateTo(form, title);
            return null;
        }
        var newInstance = form;
        return NavigateTo(newInstance, title, newInstance, null);
    }

    public static Form NavigateTo(Form form, string title, bool newForm, Dictionary<string, object> props)
    {
        if (!newForm)
            return NavigateTo(form, title, props);
        var newInstance = form;
        return NavigateTo(newInstance, title, newInstance, props);
    }

    public static Form NavigateTo(Form form, string title, Form parentForm, Dictionary<string, object> props)
    {
        form.Text = title;
        if (props != null && form is IPropsReceiver receiver)
        {
            receiver.SetProps(props);
        }
        form.Show();
        return form;
    }
}

