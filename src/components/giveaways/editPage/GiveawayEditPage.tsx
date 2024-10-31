import "./GiveawayEditPage.css"
import Placeholder from "../../Placeholder/Placeholder.tsx";
import {Button} from "@shadcn/components/ui/button.tsx";
import {Accordion, AccordionItem, AccordionTrigger} from "@shadcn/components/ui/accordion.tsx";
import {AccordionContent} from "@radix-ui/react-accordion";
import {ScrollArea} from "../../ui/scroll-area.tsx";
import {Label} from "@shadcn/components/ui/label.tsx";

export interface GiveawayEditViewProps {

}

export default function GiveawayEditPage(props: GiveawayEditViewProps) {
  return <div className="giveawayEditPage">
    <div className="tileBar">
      <Button variant="default">Save</Button>
      Edit: !testGW
    </div>
    <ScrollArea className="contentBorder">
        <div className="formContent">
          <div className="column">
            <h1>Primary Fields</h1>
            <div>
              <Label>

              </Label>
            </div>
          </div>
          <div className="column">
            <h1>Reminder Timer</h1>
            <div></div>
            <h1>Public Website</h1>
            <div></div>
          </div>
          <div className="column">
            <div className="winnersList">
              <h1>Winner List</h1>
            </div>
            <div className="logs">
              <h1>Logs</h1>
            </div>
          </div>
          {/*<Placeholder/>*/}
        </div>
    </ScrollArea>
  </div>
}