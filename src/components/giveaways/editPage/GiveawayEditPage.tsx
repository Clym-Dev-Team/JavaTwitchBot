import "./GiveawayEditPage.css"
import Placeholder from "../../Placeholder/Placeholder.tsx";

export interface GiveawayEditViewProps {

}

export default function GiveawayEditPage(props: GiveawayEditViewProps) {
  return <div className="giveawayEditPage">
    <div className="tileBar">Edit: !testGW</div>
    <div className="contentBorder">
      <div className="formContent">
        <Placeholder/>
      </div>
    </div>
  </div>
}