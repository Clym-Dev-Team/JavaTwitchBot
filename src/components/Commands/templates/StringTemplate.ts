export interface TemplateVar {
  name: string,
  type: string,
}

export interface StringTemplate {
  vars: TemplateVar[],
  template: string,
}