import "./CommandEditSheet.css"
import {Input} from "../../../../@shadcn/components/ui/input.tsx";
import VLabel from "../../../common/VerticalLabel/VLabel.tsx";
import TemplateEditor from "../templates/TemplateEditor.tsx";
import {Command, CooldownTypes} from "./Command.ts";
import CheckBox from "../../../common/CheckBox/CheckBox.tsx";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "../../../../@shadcn/components/ui/select.tsx";
import {Button} from "../../../../@shadcn/components/ui/button.tsx";
import {SheetFooter} from "../../../../@shadcn/components/ui/sheet.tsx";
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
import InputUnit from "../../../common/InputUnit/InputUnit.tsx";

export interface CommandFormProps {
  command: Command,
  isNew?: boolean
  onSubmit: (command: Command) => void
  onDelete: (commandId: string) => void
}

export function CommandForm({command, isNew, onSubmit, onDelete}: CommandFormProps) {
  const USERCOMMANDPREFIX = "userCommand.";

  const {handleSubmit, register, control, setValue, getValues} = useForm<Command>({
    defaultValues: command,
  });
  const {fields, append, update, remove} = useFieldArray({name: "patterns", control})

  function submit(formCommand: Command) {
    if (isNew) {
      formCommand.id = USERCOMMANDPREFIX + formCommand.id
      formCommand.template.id = formCommand.id;
    } else {
      // if form field is disabled the value will not exist in the command object from the form
      formCommand.id = command.id
    }
    onSubmit(formCommand);
  }

  return <div className="commandPopup">
    <VLabel name="Internal Command Name/Id:">
      <Input id="commandId" type="text" {...register("id", {required: true, disabled: !isNew})} />
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
        <InputUnit unitType={CooldownTypes} unitFieldValue={getValues("globalCooldown.type")} onUnitChange={unitType => setValue("globalCooldown.type", unitType)} registerValue={register("globalCooldown.amount", {required: true, min: 0, valueAsNumber: true})}/>
      </VLabel>
      <VLabel name="User Cooldown:">
        <InputUnit unitType={CooldownTypes} unitFieldValue={getValues("userCooldown.type")} onUnitChange={unitType => setValue("userCooldown.type", unitType)} registerValue={register("userCooldown.amount", {required: true, min: 0, valueAsNumber: true})}/>
      </VLabel>
    </div>

    <VLabel name="Template:">
      <TemplateEditor template={getValues("template")}/>
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