import {useEffect, useState} from "react";
import {Template} from "../Template.ts";
import Loader from "../../../common/LoadingSpinner/Loader.tsx";
import TemplateListItem from "./TemplateListItem.tsx";
import {getAllTemplates} from "../TemplateClient.ts";
import "./TemplateList.css"
import TitleBar from "../../TitleBar/TitleBar.tsx";

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
      <TitleBar title="List Templates"/>
      <div className="items">
        {loading ? <Loader/> : templates?.map(
          (template, key) =>
            <TemplateListItem template={template} key={key}/>
        )}
      </div>
    </div>
  )
}