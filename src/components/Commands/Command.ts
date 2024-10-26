export interface Trigger {
  pattern: string,
  isRegex: boolean,
  isVisible: boolean,
  isEnabled: boolean,
}

export enum CommandPermission {
  EVERYONE,
  PREDICTIONS_BLUE,
  PREDICTIONS_PINK,
  SUBSCRIBER,
  ARTIST,
  FOUNDER,
  VIP,
  MODERATOR,
  BROADCASTER,
  OWNER,
  SYSTEM
}

export enum CooldownTypes {
  MESSAGE,
  SECONDS
}

export interface CommandCooldown {
  value: number,
  type: CooldownTypes
}

export interface TemplateVar {
  name: string,
  type: string,
}

export interface StringTemplate {
  vars: TemplateVar[],
  template: string,
}

export interface Command {
  id: string,
  trigger: Trigger[],
  permission: string,
  userCooldown: CommandCooldown,
  globalCooldown: CommandCooldown,
  templateVar: StringTemplate,
}