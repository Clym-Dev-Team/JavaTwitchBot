import CommandEditSheet from "./CommandEditSheet.tsx";
import {Command, CommandPermission, CooldownTypes} from "./Command.ts";

export default function CommandList() {
  const commmand2: Command = {
    id: "testCommand name",
    trigger: [
      {isEnabled: true, isRegex: false, isVisible: true, pattern: "!testCommand"}
    ],
    permission: CommandPermission.EVERYONE,
    globalCooldown: {type: CooldownTypes.SECONDS, value: 0},
    userCooldown: {type: CooldownTypes.MESSAGE, value: 1},
    templateVar: {vars: [{name: "testvar", type: "string"}], template: 'test command ${testvar}}'}
  };
  return <div className="commandList">
    <CommandEditSheet command={{}}>Command 1</CommandEditSheet>
    <br/>
    <CommandEditSheet command={commmand2}>Command 2</CommandEditSheet>
  </div>
}