import {useEffect, useState} from "react";
import {Template} from "../Template.ts";
import Loader from "../../LoadingSpinner/Loader.tsx";
import TemplateListItem from "./TemplateListItem.tsx";
import {getAllTemplates} from "../TemplateClient.ts";
import "./TemplateList.css"

export default function TemplateListPane() {
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
          <TemplateListItem template={template} key={key}/>
      )}
      </div>
    </div>
  )
}