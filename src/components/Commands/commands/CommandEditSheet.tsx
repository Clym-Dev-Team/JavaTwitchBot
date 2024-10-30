import "./CommandEditSheet.css"
import {Input} from "@shadcn/components/ui/input.tsx";
import VLabel from "../../../common/VerticalLabel/VLabel.tsx";
import TemplateEditor from "../templates/TemplateEditor.tsx";
import {Command} from "./Command.ts";
import CheckBox from "../../../common/CheckBox/CheckBox.tsx";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@shadcn/components/ui/select.tsx";
import {Button} from "@shadcn/components/ui/button.tsx";
import {SheetFooter} from "@shadcn/components/ui/sheet.tsx";
import {
  useForm,
  useFieldArray,
  UseFieldArrayRemove,
  UseFieldArrayUpdate,
  UseFormRegister,
  FieldArrayWithId
} from "react-hook-form";
import IconX from "../../../assets/IconX.tsx";
import EnabledCheckBox from "../common/EnabledCheckBox.tsx";
import IsVisibleCheckBox from "../common/IsVisibleCheckbox.tsx";

export interface CommandFormProps {
  command: Command,
  isEdit?: boolean
  onSubmit: (command: Command) => void
  onDelete: (commandId: string) => void
}

export function CommandForm({command, isEdit, onSubmit, onDelete}: CommandFormProps) {
  const USERCOMMANDPREFIX = "userCommand.";
  const hasPrefix = false;
  if (command.id != null && command.id.startsWith(USERCOMMANDPREFIX)) {
    command.id = command.id.slice(USERCOMMANDPREFIX.length);
  }

  const {handleSubmit, register, control, setValue, getValues} = useForm<Command>({
    defaultValues: command,
  });
  const {fields, append, update, remove} = useFieldArray({name: "patterns", control})

  function submit(command: Command) {
    if (hasPrefix || !isEdit) {
      command.id = USERCOMMANDPREFIX + command.id
      command.template.id = command.id;
    }
    onSubmit(command);
  }

  return <div className="commandPopup">
    <VLabel name="Internal Command Name/Id:">
      <Input id="commandId" type="text" {...register("id", {required: true, disabled: isEdit})} />
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
      <Button variant={"destructive"} onClick={() => onDelete(command.id)}>Delete</Button>
      <Button variant={"default"} onClick={handleSubmit(submit)}>Save</Button>
    </SheetFooter>
  </div>
}


function TriggerInput(index: number, field: FieldArrayWithId<Command, "patterns">, register: UseFormRegister<Command>, update: UseFieldArrayUpdate<Command, "patterns">, remove: UseFieldArrayRemove) {
  return <div className="trigger" key={index}>
    <Button className="removeTriggerBtn" onClick={() => remove(index)}><IconX/></Button>
    <Input type="text" placeholder="Trigger Pattern" {...register(`patterns.${index}.pattern`, {required: true})}/>
    <CheckBox checked={field.isRegex} onChange={checked => update(index, {...field, isRegex: checked})}
              hoverText="Regex Trigger"/>
    <IsVisibleCheckBox checked={field.isVisible} onChange={checked => update(index, {...field, isVisible: checked})}
                       hoverText="Visible in Command List"/>
    <EnabledCheckBox checked={field.isEnabled} onChange={checked => update(index, {...field, isEnabled: checked})}
                     hoverText="Enabled"/>
  </div>
}