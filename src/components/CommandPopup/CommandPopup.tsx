import "./CommandPopup.css"
import {Input} from "@shadcn/components/ui/input.tsx";
import {Label} from "@shadcn/components/ui/label.tsx";


export interface CommandPopupProps {

}

export default function CommandPopup(props: CommandPopupProps) {
  return <div className="commandPopup">
    <Label htmlFor="commandId">Internal Command Nane/Id:</Label>
    <Input id="commandId" type="text"/>
    <div className="triggers">
      Trigger:
      <div className="trigger">
        <Input type="text" placeholder="Trigger Pattern"/>
        <input type="checkbox" alt="Regex Trigger"/>
        <input type="checkbox" alt="Visible in Command List"/>
        <input type="checkbox" alt="Enabled"/>
      </div>
      <div className="addTrigger">Add a new Alias</div>
    </div>
    <label>
      Required Permission Level:
      <select>
        <option>All</option>
        <option>Follower</option>
        <option>Mods</option>
        <option>Owner</option>
      </select>
    </label>
    <div className="cooldown">
      <label>
        Global Cooldown:
        <input type="text"/>
      </label>
      <label>
        User Cooldown:
        <input type="text"/>
      </label>
    </div>
    <div className="templateSpacer">TEMPLATE EDIT PLACEHOLDER</div>
  </div>
}