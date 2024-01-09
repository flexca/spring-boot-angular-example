import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { TableWrapperColumnModel } from './table-wrapper-column.model';
import { Sort } from '@angular/material/sort';
import { TableWrapperFilterModel } from './table-wrapper-filter.model';
import { TableWrapperFilterType } from './table-wrapper-filter.type';
import { TableWrapperFilterValueModel } from './table-wrapper-filter-value.model';
import { TableWrapperFilterDateModel } from './table-wrapper-filter-date.model';
import { TableWrapperFilterStateModel } from './table-wrapper-filter-state.model';
import { formatDate } from "@angular/common";

@Component({
  selector: 'app-table-wrapper',
  templateUrl: './table-wrapper.component.html',
  styleUrls: ['./table-wrapper.component.scss']
})
export class TableWrapperComponent implements OnInit {

  @Input() columns: Array<TableWrapperColumnModel> = [];
  @Input() displayedColumns: string[] = [];
  @Input() dataSource: Array<any> = [];
  @Input() filterModel: Array<TableWrapperFilterModel> = [];
  @Input() offset = 0;
  @Input() nextPage = false;

  @Output() navigateToNextPage = new EventEmitter<void>();
  @Output() navigateToPreviousPage = new EventEmitter<void>();
  @Output() filtersOnChange = new EventEmitter<TableWrapperFilterStateModel>();
  
  DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

  selectedFilterField = "";
  selectedFilterTitle = "";
  selectedFilterValue = "";
  selectedFilterValueStart = "";
  selectedFilterValueEnd = "";
  
  selectedFilterType: TableWrapperFilterType | undefined = undefined;
  selectedFilterData: any = undefined;

  selectedFilters: Array<TableWrapperFilterValueModel> = [];

  selectedSorting: Sort | undefined = undefined;

  readonly TableWrapperFilterType = TableWrapperFilterType;

  ngOnInit() {
    if(this.filterModel && this.filterModel.length > 0) {
      this.selectedFilterField = this.filterModel[0].id;
      this.selectedFilterTitle = this.filterModel[0].title;
      this.selectedFilterType = this.filterModel[0].type;
      this.selectedFilterData = this.filterModel[0].filterData;
    }
  }

  filterSelected(selectedItem: any) {
    const selectedFilter = this.filterModel.find(filter => filter.id === selectedItem);
    if(selectedFilter) {
      this.selectedFilterField = selectedFilter.id;
      this.selectedFilterTitle = selectedFilter.title;
      this.selectedFilterType = selectedFilter.type;
      this.selectedFilterData = selectedFilter.filterData;
    }
    this.selectedFilterValue = "";
    this.selectedFilterValueStart = "";
    this.selectedFilterValueEnd = "";
  }

  onSortChange(sortState: Sort) {
    this.selectedSorting = sortState;
    const event = new TableWrapperFilterStateModel(this.selectedFilters, this.selectedSorting);
    this.filtersOnChange.emit(event);
  }

  applyFilter() {
    if(this.selectedFilterField && this.selectedFilterType != undefined && this.isNotEmptyFilterValue()) {
      let filterValue: any;
      if(TableWrapperFilterType.date == this.selectedFilterType) {
        let start = undefined;
        if(this.selectedFilterValueStart) {
          start = formatDate(this.selectedFilterValueStart, this.DATE_TIME_FORMAT, 'en-US');
        }
        let end = undefined;
        if(this.selectedFilterValueEnd) {
          end = formatDate(this.selectedFilterValueEnd, this.DATE_TIME_FORMAT, 'en-US');
        }
        filterValue = new TableWrapperFilterDateModel(start, end);
      } else {
        filterValue = this.selectedFilterValue;
      }

      const selectedFilter = new TableWrapperFilterValueModel(this.selectedFilterField, this.selectedFilterTitle, 
        this.selectedFilterType, filterValue);
      this.selectedFilters.push(selectedFilter);
      const availableFilters = this.getAvailableFilters();
      if(availableFilters.length > 0) {
        this.selectedFilterField = availableFilters[0].id;
        this.selectedFilterTitle = availableFilters[0].title;
        this.selectedFilterType = availableFilters[0].type;
        this.selectedFilterData = availableFilters[0].filterData;
      } else {
        this.selectedFilterField = "";
      }
      this.selectedFilterValue = "";
      this.selectedFilterValueStart = "";
      this.selectedFilterValueEnd = "";
      const event = new TableWrapperFilterStateModel(this.selectedFilters, this.selectedSorting);
      this.filtersOnChange.emit(event);
    }
  }

  remove(toRemove: TableWrapperFilterValueModel) {
    const toRemoveIndex = this.selectedFilters.findIndex(item => item.id === toRemove.id);
    if(toRemoveIndex >= 0) {
      this.selectedFilters.splice(toRemoveIndex, 1);
      const event = new TableWrapperFilterStateModel(this.selectedFilters, this.selectedSorting);
      this.filtersOnChange.emit(event);
    }
    if(!this.selectedFilterField || this.selectedFilterField.length <= 0) {
      const availableFilters = this.getAvailableFilters();
      if(availableFilters.length > 0) {
        this.selectedFilterField = availableFilters[0].id;
        this.selectedFilterTitle = availableFilters[0].title;
        this.selectedFilterType = availableFilters[0].type;
        this.selectedFilterData = availableFilters[0].filterData;
        this.selectedFilterValue = "";
        this.selectedFilterValueStart = "";
        this.selectedFilterValueEnd = "";
      }
    }
  }

  isNotEmptyFilterValue(): boolean {
    if(TableWrapperFilterType.date == this.selectedFilterType) {
      return (this.selectedFilterValueStart != undefined && this.selectedFilterValueStart != undefined) 
        || (this.selectedFilterValueEnd != undefined && this.selectedFilterValueEnd != undefined);
    } else {
      return (this.selectedFilterValue != undefined && this.selectedFilterValue.length > 0) ;
    }
  }

  getAvailableFilters(): Array<TableWrapperFilterModel> {
    return this.filterModel.filter(item => !this.isSelectedFilter(item.id));
  }

  isSelectedFilter(id: string): boolean {
    return this.selectedFilters.find(item => item.id === id) != undefined;
  }

  toPreviousPage() {
    this.navigateToPreviousPage.emit();
  }

  toNextPage() {
    this.navigateToNextPage.emit();
  }
}
