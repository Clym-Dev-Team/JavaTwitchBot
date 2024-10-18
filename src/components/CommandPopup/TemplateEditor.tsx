import {StringTemplate} from "./Command.ts";

export interface TemplateEditorProps {
  template: StringTemplate
}

export default function TemplateEditor({template}: TemplateEditorProps) {
  return <div className="templateEditor">
    <div className="templateVars">
      {template.vars.map((tVar) => <span>{tVar.name}: {tVar.type}</span>)}
    </div>
    <div className="templateBody">
      <textarea value={template.template}/>
    </div>
  </div>
}