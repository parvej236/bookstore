const DD_MM_YYYY_PATTERN = '(?:((?:0[1-9]|1[0-9]|2[0-9])\/(?:0[1-9]|1[0-2])|(?:30)\/(?!02)(?:0[1-9]|1[0-2])|31\/(?:0[13578]|1[02]))\/(?:19|20)[0-9]{2})'
const DATE_FORMAT = 'dd/mm/yyyy'


function applyDatepicker(el, val, minDate, changeCallback) {
  el.autocomplete = 'off'

  const options = {
    autohide: true,
    format: DATE_FORMAT,
    minDate
  }

  const datepicker = new Datepicker(el, options);

  datepicker.setDate(val)

  changeCallback && el.addEventListener('changeDate', function (e) {
    changeCallback(e)
  });

  return datepicker
}
function applyDateRangePicker(el, rangeStart, rangeEnd, minDate, rangeStartCallback, rangeEndCallback) {
  const options = {
    autohide: true,
    format: DATE_FORMAT,
    minDate
  }

  const dateRangePicker = new DateRangePicker(el, options);

  dateRangePicker.setDates(rangeStart, rangeEnd)

  rangeStartCallback && dateRangePicker.inputs[0].addEventListener('changeDate', function (e) {
    rangeStartCallback(e)
  });
  rangeEndCallback && dateRangePicker.inputs[1].addEventListener('changeDate', function (e) {
    rangeEndCallback(e)
  });

  return dateRangePicker
}