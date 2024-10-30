import "./GiveawayListPage.css"
import GiveawayList from "./GiveawayList.tsx";
import GiveawayTemplateList from "./GiveawayTemplateList.tsx";

export default function GiveawayListPage() {
  return <div className="giveawayListView">
    <GiveawayList/>
    <div className="coloumn2">
      <GiveawayTemplateList/>
      <div className="spacer">PLACEHOLDER</div>
    </div>
  </div>
}