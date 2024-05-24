import {useEffect, useState} from "react";
import {Template} from "../Template.ts";
import Loader from "../../LoadingSpinner/Loader.tsx";
import TemplateListItem from "./TemplateListItem.tsx";
import {getAllTemplates} from "../TemplateClient.ts";

export interface TemplateListProps {
  onSelect: (template: Template) => void;
}

export default function TemplateList(props: TemplateListProps) {
  const [templates, setTemplates] = useState<Template[]>();
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getAllTemplates()
      .then(r => setTemplates(r))
      .catch(reason => console.log(reason))
      .finally(() => setLoading(false));
  }, [])

  return (
    <div className="templateList">
      <div className="titleBar"></div>
      <div className="items">
      {loading ? <Loader/> : templates?.map(
        (template, key) =>
          <TemplateListItem template={template} key={key} onSelect={props.onSelect}/>
      )}
      </div>
    </div>
  )
}