import "./GiveawayTemplateList.css"
import {Giveaway} from "../Giveaway.ts";
import {Button} from "@shadcn/components/ui/button.tsx";
import IconGear from "../../../assets/IconGear.tsx";

export interface GiveawayTemplateListProps {
}

interface TemplateItem {
  displayName: string;
  id: string,
}

export default function GiveawayTemplateList({}: GiveawayTemplateListProps) {
  const templateList: TemplateItem[] = [
    {
      displayName: "test Template 1",
      id: "test1"
    },
    {
      displayName: "Goldkette",
      id: "goldkette"
    },
    {
      displayName: "Steam Key",
      id: "steamKey"
    }
  ]


  return <div className="giveawayTemplateList">
    <div>Giveaway Templates:</div>
    {templateList.map(template => <Button className="templateItem" key={template.id} variant="secondary">
      <div>{template.displayName}</div>
      <Button className="templateEditBtn" variant="outline"><IconGear/></Button>
    </Button>)}
  </div>
}


function dummyActive(): Giveaway[] {
  return [];
  // return [{
  //   title: "testGW",
  //   id: "2024-10-29_testGW",
  //   commandPattern: "!test",
  //   startTime: Date.now(),
  //   status: GiveawayStatus.CREATED,
  //   ticketCost: 10,
  //   maxTickets: 50,
  //
  //
  // }]
}