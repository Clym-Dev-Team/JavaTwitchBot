import "./CommandPopup.css"
import {Input} from "@shadcn/components/ui/input.tsx";
import VLabel from "../../common/VerticalLabel/VLabel.tsx";
import IconCheckBox from "../../common/IconCheckBox/IconCheckBox.tsx";
import IconList from "../../assets/IconList.tsx";
import IconHidden from "../../assets/IconHidden.tsx";
import {useState} from "react";


export interface CommandPopupProps {

}

export default function CommandPopup(props: CommandPopupProps) {
  const [triggerChecked, setTriggerChecked] = useState(false);

  return <div className="commandPopup">
    <VLabel name="Internal Command Nane/Id:">
      <Input id="commandId" type="text"/>
    </VLabel>
    <div className="triggers">
      Trigger:
      <div className="trigger">
        <Input type="text" placeholder="Trigger Pattern"/>
        <input type="checkbox" alt="Regex Trigger"/>
        <IconCheckBox checked={triggerChecked} onChange={setTriggerChecked} checkedIcon={<IconList/>} icon={<IconHidden/>} hoverText="Visible in Command List"/>
        <input type="checkbox" alt="Enabled"/>
      </div>
      <div className="addTrigger">Add a new Alias</div>
    </div>
    <VLabel name="Required Permission Level:">
      <select>
        <option>All</option>
        <option>Follower</option>
        <option>Mods</option>
        <option>Owner</option>
      </select>
    </VLabel>
    <div className="cooldown">
      <VLabel name="Global Cooldown:">
        <Input type="text"/>
      </VLabel>
      <VLabel name="User Cooldown:">
        <Input type="text"/>
      </VLabel>
    </div>
    <div className="templateSpacer">TEMPLATE EDIT PLACEHOLDER</div>
  </div>
}