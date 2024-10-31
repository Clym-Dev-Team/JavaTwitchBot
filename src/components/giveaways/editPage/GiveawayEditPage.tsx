import "./GiveawayEditPage.css"
import Placeholder from "../../Placeholder/Placeholder.tsx";
import {Button} from "@shadcn/components/ui/button.tsx";

export interface GiveawayEditViewProps {

}

export default function GiveawayEditPage(props: GiveawayEditViewProps) {
  return <div className="giveawayEditPage">
    <div className="tileBar">
      <Button variant="default">Save</Button>
      Edit: !testGW</div>
    <div className="contentBorder">
      <div className="formContent">
        <Placeholder/>
      </div>
    </div>
  </div>
}