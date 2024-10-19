import "./CommandPopup.css"
import {Input} from "@shadcn/components/ui/input.tsx";
import VLabel from "../../common/VerticalLabel/VLabel.tsx";
import IconCheckBox from "../../common/IconCheckBox/IconCheckBox.tsx";
import IconList from "../../assets/IconList.tsx";
import IconHidden from "../../assets/IconHidden.tsx";
import {useState} from "react";
import TemplateEditor from "./TemplateEditor.tsx";
import {Command, CommandPermission} from "./Command.ts";
import CheckBox from "../../common/CheckBox/CheckBox.tsx";
import IconPowerOff from "../../assets/IconPowerOff.tsx";
import IconPowerOn from "../../assets/IconPowerOn.tsx";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue
} from "@shadcn/components/ui/select.tsx";
import {Button} from "@shadcn/components/ui/button.tsx";


export interface CommandPopupProps {

}

export default function CommandPopup(props: Command) {
  const [triggerChecked, setTriggerChecked] = useState(false);

  return <div className="commandPopup">
    <VLabel name="Internal Command Nane/Id:">
      <Input id="commandId" type="text"/>
    </VLabel>

    <div className="triggers">
      Trigger:
      <div className="trigger">
        <Input type="text" placeholder="Trigger Pattern"/>
        <CheckBox checked={triggerChecked} onChange={setTriggerChecked} hoverText="Regex Trigger"/>
        <IconCheckBox checked={triggerChecked} onChange={setTriggerChecked} checkedIcon={<IconList/>}
                      icon={<IconHidden/>} hoverText="Visible in Command List"/>
        <IconCheckBox checked={triggerChecked} onChange={setTriggerChecked} hoverText="Enabled"
                      icon={<IconPowerOff/>} checkedIcon={<IconPowerOn/>}/>
      </div>
      <Button variant="secondary" className="addTrigger">Add a new Alias</Button>
    </div>

    <VLabel name="Required Permission Level:">
      <Select>
        <SelectTrigger>
          <SelectValue placeholder="Select a Permission Level"/>
        </SelectTrigger>
        <SelectContent>
          <SelectItem value={"EVERYONE"}>EVERYONE</SelectItem>
          <SelectItem value={"VIP"}>VIP</SelectItem>
          <SelectItem value={"MODERATOR"}>MODERATOR</SelectItem>
          <SelectItem value={"OWNER"}>OWNER</SelectItem>
        </SelectContent>
      </Select>
    </VLabel>

    <div className="cooldown">
      <VLabel name="Global Cooldown:">
        <Input type="text"/>
      </VLabel>
      <VLabel name="User Cooldown:">
        <Input type="text"/>
      </VLabel>
    </div>

    <VLabel name="Template:">
      <TemplateEditor template={{template: "tetst", vars: [{name: "testvar", type: "string"}]}}/>
      {/*<div className="templateSpacer">TEMPLATE EDIT PLACEHOLDER</div>*/}
    </VLabel>
  </div>
}