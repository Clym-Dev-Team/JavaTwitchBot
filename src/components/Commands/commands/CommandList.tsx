import {CommandForm} from "./CommandEditSheet.tsx";
import {Command, CommandPermission, CooldownTypes} from "./Command.ts";
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@shadcn/components/ui/table.tsx";
import {Sheet, SheetContent, SheetDescription, SheetHeader, SheetTitle} from "@shadcn/components/ui/sheet.tsx";
import {useState} from "react";

export default function CommandList() {
  const [openCommand, setOpenCommand] = useState<Command | undefined>(undefined)
  const commmand2: Command = {
    id: "testCommand name",
    trigger: [
      {isEnabled: true, isRegex: false, isVisible: true, pattern: "!testCommand"}
    ],
    permission: CommandPermission[CommandPermission.EVERYONE],
    globalCooldown: {type: CooldownTypes.SECONDS, value: 0},
    userCooldown: {type: CooldownTypes.MESSAGE, value: 1},
    templateVar: {vars: [{name: "testvar", type: "string"}], template: 'test command ${testvar}}'}
  };
  const command3: Command = {...commmand2, id: "test.trigger1"}
  const commands: Command[] = [commmand2, command3, command3]



  return <div className="commandList">
    <Table>
      <TableHeader>
        <TableHead>Enabled</TableHead>
        <TableHead>Visible</TableHead>
        <TableHead>Name/Id</TableHead>
        <TableHead>A Pattern</TableHead>
        <TableHead>Template</TableHead>
      </TableHeader>
      <TableBody>
        {commands.map((command) => (
          <TableRow onClick={() => {
            console.log("open Modal");
            setOpenCommand(command)}}>
              <TableCell>{command.trigger[0].isEnabled}</TableCell>
              <TableCell>{command.trigger[0].isVisible}</TableCell>
              <TableCell>{command.id}</TableCell>
              <TableCell>{command.trigger[0].pattern}</TableCell>
              <TableCell>{command.templateVar.template}</TableCell>
          </TableRow>
        ))}
      </TableBody>
    </Table>
    <Sheet open={openCommand != undefined} onOpenChange={open => {console.log("sheet open: " + open); !open && setOpenCommand(undefined)}}>
      <SheetContent style={{minWidth: "40%", overflowY: "auto"}} className="dark">
        <SheetHeader>
          <SheetTitle>Edit Command:</SheetTitle>
          <SheetDescription>Edit a command. All empty trigger will be ignored</SheetDescription>
        </SheetHeader>
        { openCommand ? <CommandForm command={openCommand}/>: ""}
      </SheetContent>
    </Sheet>
  </div>
}