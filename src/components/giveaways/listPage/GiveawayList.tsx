import {ScrollArea} from "../../ui/scroll-area.tsx";
import GiveawayTile from "./GiveawayTile.tsx";
import {Accordion, AccordionContent, AccordionItem, AccordionTrigger} from "@shadcn/components/ui/accordion.tsx";

export interface GiveawayListProps {

}

export default function GiveawayList(props: GiveawayListProps) {
  return <ScrollArea className="giveawayList">
    <div className="activeList">
      <GiveawayTile/>
      <GiveawayTile/>
      <GiveawayTile/>
    </div>
    <Accordion type="single">
      <AccordionItem value="item-1">
        <AccordionTrigger>Archived Giveaways</AccordionTrigger>
        <AccordionContent className="archivedList">
          <GiveawayTile isArchived={true}/>
          <GiveawayTile isArchived={true}/>
          <GiveawayTile isArchived={true}/>
          <GiveawayTile isArchived={true}/>
          <GiveawayTile isArchived={true}/>
        </AccordionContent>
      </AccordionItem>
    </Accordion>
  </ScrollArea>;
}