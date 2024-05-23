import {Template} from "../Template.ts";
import "./TemplateListItem.css"

export interface TemplateListItemProps {
  template: Template
}

export default function TemplateListItem(props: TemplateListItemProps) {
  return (
    <div className="template_list-item">
      <span className="identifier">
        <div className="module">module = {props.template.module}</div>
        <div className="name">type = {props.template.type}</div>
        <div className="object-name">object = {props.template.object}</div>
      </span>
      <div className="template_string">temp = {props.template.template}</div>
    </div>
  )

}