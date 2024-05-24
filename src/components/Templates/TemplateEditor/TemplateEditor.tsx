import {Template} from "../Template.ts";

export interface TemplateEditorProps {
  template: Template;
  onClose: () => void
}

export default function TemplateEditor(props: TemplateEditorProps) {
  return <div className="templateEditor">
    <div className="titleBar">
      <div className="returnBtn">
        <button onClick={props.onClose}>Return</button>
      </div>
    </div>
  </div>
}