import {useEffect, useState} from "react";
import {Template} from "../Template.ts";
import {getAllTemplates} from "../TemplateClient.ts";
import TemplateListItem from "../TemplateList/TemplateListItem.tsx";
import Loader from "../../LoadingSpinner/Loader.tsx";

export function TemplatePane() {
  const [templates, setTemplates] = useState<Template[]>();
  const [loading, setLoading] = useState(true);
  
  useEffect(() => {
    getAllTemplates()
      .then(r => setTemplates(r))
      .catch(reason => console.log(reason))
      .finally(() => setLoading(false));
  }, [])

  return (
    <div className="template-pane">
      {loading ? <Loader/> : templates?.map(
      (template, key) =>
        <TemplateListItem template={template} key={key}/>
    )}
    </div>
  )
}