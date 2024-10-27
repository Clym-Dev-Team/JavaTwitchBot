export interface TemplateVar {
  name: string,
  type: string,
}

export interface StringTemplate {
  vars: TemplateVar[],
  id: string
  template: string,
}