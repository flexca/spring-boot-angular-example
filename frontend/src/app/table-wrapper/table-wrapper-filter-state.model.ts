import { Sort } from "@angular/material/sort";
import { TableWrapperFilterValueModel } from "./table-wrapper-filter-value.model";

export class TableWrapperFilterStateModel {

    constructor(
        public filters: Array<TableWrapperFilterValueModel>,
        public sorting?: Sort) {  
    }

    public findFilter(id: string): TableWrapperFilterValueModel | undefined {
        if(this.filters) {
            return this.filters.find(item => item.id == id);
        }
        return undefined;
    }
}