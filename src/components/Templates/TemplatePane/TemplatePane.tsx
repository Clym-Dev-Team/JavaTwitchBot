import {useState} from "react";
import {Template} from "../Template.ts";
import TemplateEditor from "../TemplateEditor/TemplateEditor.tsx";
import TemplateList from "../TemplateList/TemplateList.tsx";

export function TemplatePane() {
  const [selectedTemplate, setSelectedTemplate] = useState<Template | null>(null);

  function handleListSelect(template: Template) {
    setSelectedTemplate(template);
  }

  function handleEditorClose() {
    setSelectedTemplate(null);
  }

  if (selectedTemplate == null) {
    return <TemplateList onSelect={handleListSelect}/>
  } else {
    return <TemplateEditor template={!!selectedTemplate && selectedTemplate} onClose={handleEditorClose}/>
  }
}