export class Dialog {
    title: string = '';
    component: any;
    props: any = {}

    private constructor(title: string, component: any, props: any) {
        this.title = title;
        this.component = component;
        this.props = props;
    }

    static create(title: string, component: any, props: any = {}): Dialog {
        return new Dialog(title, component, props);
    }
}
