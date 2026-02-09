using System.Xml;
using System.Xml.Linq;
using ExempleCurs12.domain;

namespace ExempleCurs12.xmlExamples;

public class XmlExample
{
    public static void Run()
    {
        XmlWriterSettings settings = new XmlWriterSettings()
        {
            Indent = true,
        };

        XmlWriter xmlWriter = XmlWriter.Create("ducks.xml", settings);    
        
        xmlWriter.WriteStartDocument();
        xmlWriter.WriteStartElement("ducks");
        
        xmlWriter.WriteStartElement("Duck");
        xmlWriter.WriteAttributeString("id", "3");
        
        xmlWriter.WriteStartElement("Name");
        xmlWriter.WriteString("Petra");
        xmlWriter.WriteEndElement();
        
        xmlWriter.WriteStartElement("Speed");
        xmlWriter.WriteString("5");
        xmlWriter.WriteEndElement();
        
        xmlWriter.WriteStartElement("Resistance");
        xmlWriter.WriteString("5");
        xmlWriter.WriteEndElement();
        
        xmlWriter.WriteEndElement();
        xmlWriter.Close();
        
        XDocument document = XDocument.Load("ducks.xml");
        var ducks = from d in document.Descendants("Duck")
            select new Duck(
                int.Parse(d.Attribute("id").Value), 
                d.Element("Name").Value, 
                double.Parse(d.Element("Speed").Value), 
                double.Parse(d.Element("Resistance").Value)
                );
        
        foreach (var duck in ducks)
            Console.WriteLine(duck.ToString());
            
    }
    
    
}