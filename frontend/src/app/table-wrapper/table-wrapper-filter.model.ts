import { TableWrapperFilterType } from "./table-wrapper-filter.type";

export class TableWrapperFilterModel {

    constructor(
        public id: string,
        public title: string,
        public type: TableWrapperFilterType,
        public filterData?: any) {  
    }
}