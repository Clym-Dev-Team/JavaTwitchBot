import {Template} from "../Template.ts";
import "./TemplateListItem.css"

export interface TemplateListItemProps {
  template: Template,
  onSelect: (template: Template) => void
}

export default function TemplateListItem(props: TemplateListItemProps) {
  return (
    <div className="template_list-item" onClick={() => props.onSelect(props.template)}>
      <div className="identifier">
        <span className="module">{props.template.module}</span>
        <span className="dot">.</span>
        <span className="name">{props.template.type}</span>
        <span className="dot">.</span>
        <span className="object-name">{props.template.object}</span>
      </div>
      <div className="template_string">temp = {props.template.template}</div>
    </div>
  )

}