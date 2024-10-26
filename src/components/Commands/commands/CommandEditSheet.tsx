import "./CommandEditSheet.css"
import {Input} from "@shadcn/components/ui/input.tsx";
import VLabel from "../../../common/VerticalLabel/VLabel.tsx";
import IconCheckBox from "../../../common/IconCheckBox/IconCheckBox.tsx";
import IconList from "../../../assets/IconList.tsx";
import IconHidden from "../../../assets/IconHidden.tsx";
import React from "react";
import TemplateEditor from "../templates/TemplateEditor.tsx";
import {Command} from "./Command.ts";
import CheckBox from "../../../common/CheckBox/CheckBox.tsx";
import IconPowerOff from "../../../assets/IconPowerOff.tsx";
import IconPowerOn from "../../../assets/IconPowerOn.tsx";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@shadcn/components/ui/select.tsx";
import {Button} from "@shadcn/components/ui/button.tsx";
import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetFooter,
  SheetHeader,
  SheetTitle,
  SheetTrigger
} from "@shadcn/components/ui/sheet.tsx";
import {useForm, useFieldArray, UseFieldArrayRemove, UseFieldArrayUpdate, UseFormRegister, FieldArrayWithId} from "react-hook-form";
import IconX from "../../../assets/IconX.tsx";

export interface CommandPopupProps {
  command: Command;
  children: React.ReactNode;
}

export default function CommandEditSheet({command, children}: CommandPopupProps) {
  return <Sheet>
    <SheetTrigger>{children}</SheetTrigger>
    <SheetContent style={{minWidth: "40%", overflowY: "auto"}} className="dark">
      <SheetHeader>
        <SheetTitle>Edit Command:</SheetTitle>
        <SheetDescription>Edit a command. All empty trigger will be ignored</SheetDescription>
      </SheetHeader>
      {CommandEdit(command)}
    </SheetContent>
  </Sheet>
}

function CommandEdit(command: Command) {
  const fullCommandId = command.id;
  const USERCOMMANDPREFIX = "userCommand.";
  const hasPrefix = command.id.startsWith(USERCOMMANDPREFIX);
  if (hasPrefix) {
    command.id = command.id.slice(USERCOMMANDPREFIX.length);
  }
  const {handleSubmit, register, control, setValue, getValues} = useForm<Command>({
    defaultValues: command,
  });
  const {fields, append, update, remove} = useFieldArray({name: "trigger", control})

  function submit(command: Command) {
    if (hasPrefix) {
      command.id = USERCOMMANDPREFIX + command.id
    }
    console.log("sumbit")
    console.log(command)
  }

  function handleDelete() {
    console.log("delete: " + fullCommandId);
  }

  return <div className="commandPopup">
    <VLabel name="Internal Command Name/Id:">
      <Input id="commandId" type="text" {...register("id", {required: true})} />
    </VLabel>

    <div className="triggers">
      Trigger:
      {fields.map((field, index) => TriggerInput(index, field, register, update, remove))}
      <Button variant="secondary" className="addTrigger" onClick={() => append({
        isRegex: false,
        isVisible: true,
        isEnabled: true,
        pattern: ""
      })}>Add a new Alias</Button>
    </div>

    <VLabel name="Required Permission Level:">
      <Select defaultValue={getValues().permission} onValueChange={value => setValue("permission", value)}>
        <SelectTrigger>
          <SelectValue placeholder="Select a Permission Level"/>
        </SelectTrigger>
        <SelectContent className="dark">
          <SelectItem value="EVERYONE">EVERYONE</SelectItem>
          <SelectItem value="VIP">VIP</SelectItem>
          <SelectItem value="MODERATOR">MODERATOR</SelectItem>
          <SelectItem value="OWNER">OWNER</SelectItem>
        </SelectContent>
      </Select>
    </VLabel>

    <div className="cooldown">
      <VLabel name="Global Cooldown:">
        <Input type="number" {...register("globalCooldown.value", {required: true, min: 0, valueAsNumber: true})}/>
      </VLabel>
      <VLabel name="User Cooldown:">
        <Input type="text" {...register("userCooldown.value", {required: true, min: 0, valueAsNumber: true})}/>
      </VLabel>
    </div>

    <VLabel name="Template:">
      <TemplateEditor template={{template: "tetst", vars: [{name: "testvar", type: "string"}]}}/>
      {/*<div className="templateSpacer">TEMPLATE EDIT PLACEHOLDER</div>*/}
    </VLabel>
    <SheetFooter>
      <Button variant={"destructive"} onClick={handleDelete}>Delete</Button>
      <Button variant={"default"} onClick={handleSubmit(submit)}>Save</Button>
    </SheetFooter>
  </div>
}


function TriggerInput(index: number, field: FieldArrayWithId<Command, "trigger">, register: UseFormRegister<Command>, update: UseFieldArrayUpdate<Command, "trigger">, remove: UseFieldArrayRemove) {
  return <div className="trigger" key={index}>
    <Button className="removeTriggerBtn" onClick={() => remove(index)}><IconX/></Button>
    <Input type="text" placeholder="Trigger Pattern" {...register(`trigger.${index}.pattern`, {required: true})}/>
    <CheckBox checked={field.isRegex} onChange={checked => update(index, {...field, isRegex: checked})}
              hoverText="Regex Trigger"/>
    <IconCheckBox checked={field.isVisible} onChange={checked => update(index, {...field, isVisible: checked})}
                  hoverText="Visible in Command List" checkedIcon={<IconList/>} icon={<IconHidden/>}/>
    <IconCheckBox checked={field.isEnabled} onChange={checked => update(index, {...field, isEnabled: checked})}
                  hoverText="Enabled" icon={<IconPowerOff/>} checkedIcon={<IconPowerOn/>}/>
  </div>
}