class AutoComplete {
    constructor(el, renderItem, handleSelectedItem) {
        if (typeof el === 'string') {
            this.el = document.querySelector(el)
        } else {
            this.el = el
        }
        this.renderItem = renderItem
        this.handleSelectedItem = handleSelectedItem

        this.currentFocus = -1


        this.el.setAttribute('id', this.generateID(10))

        this.el.addEventListener('input', e => this.search(e))
        this.el.addEventListener('keydown', e => this.selectItem(e))

        document.addEventListener("click", e => this.closeAllLists(e.target));
    }

    search(e) {
        this.closeAllLists()
        const that = this
        this.currentFocus = -1

        if (!(this.el.value && this.el.value.trim() !== '' && this.el.value.length > 2)) {
            return
        }

        const autoCompleteList = document.createElement('DIV')
        autoCompleteList.setAttribute('id', `${this.el.id}-autocomplete-list`)
        autoCompleteList.classList.add('autocomplete-items', 'shadow', 'rounded')


        this.el.parentNode.insertBefore(autoCompleteList, this.el)

        fetch(`${this.el.dataset.searchUrl}${this.el.value}`)
            .then(response => response.json())
            .then(data => {
                that.createList(autoCompleteList, data)
            })

    }

    createList(listElement, list = []) {
        const that = this
        for (const item of list) {
            const itemEl = document.createElement('DIV')
            itemEl.classList.add('autocomplete-list-item')
            // itemEl.dataset.value = item.id
            itemEl.innerHTML = this.renderItem(this.el.value, item)
            itemEl.addEventListener("click", function(e) {
                /*insert the value for the autocomplete text field:*/
                that.handleSelectedItem(that.el, item)
                /*close the list of autocompleted values,
                (or any other open lists of autocompleted values:*/
                that.closeAllLists();
            })
            listElement.appendChild(itemEl)
        }
    }

    selectItem(e) {
        let listEl = document.querySelector(`#${this.el.id}-autocomplete-list`)
        if (listEl) listEl = listEl.querySelectorAll('.autocomplete-list-item')

        if (e.keyCode === 40) {
            /*If the arrow DOWN key is pressed,
            increase the currentFocus variable:*/
            this.currentFocus++;
            /*and and make the current item more visible:*/
            this.addActive(listEl);
        } else if (e.keyCode === 38) { //up
            /*If the arrow UP key is pressed,
            decrease the currentFocus variable:*/
            this.currentFocus--;
            /*and and make the current item more visible:*/
            this.addActive(listEl);
        } else if (e.keyCode === 13) {
            /*If the ENTER key is pressed, prevent the form from being submitted,*/
            e.preventDefault();
            if (this.currentFocus > -1) {
                /*and simulate a click on the "active" item:*/
                if (listEl) listEl[this.currentFocus].click();
            }
        }
    }

    addActive(els) {
        /*a function to classify an item as "active":*/
        if (!els) return false;
        /*start by removing the "active" class on all items:*/
        this.removeActive(els);
        if (this.currentFocus >= els.length) this.currentFocus = 0;
        if (this.currentFocus < 0) this.currentFocus = (els.length - 1);
        /*add class "autocomplete-active":*/
        els[this.currentFocus].classList.add("autocomplete-active");
    }

    removeActive(els) {
        /*a function to remove the "active" class from all autocomplete items:*/
        for (const el of els) {
            el.classList.remove("autocomplete-active");
        }
    }

    closeAllLists(el) {
        /*close all autocomplete lists in the document,
        except the one passed as an argument:*/
        const lists = document.querySelectorAll(".autocomplete-items");
        for (const list of lists) {
            if (el !== list && el !== this.el) {
                list.parentNode.removeChild(list);
            }
        }
    }

    generateID(length) {
        let result           = '';
        const characters       = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        const charactersLength = characters.length;
        for ( let i = 0; i < length; i++ ) {
            result += characters.charAt(Math.floor(Math.random() *
                charactersLength));
        }
        return result;
    }

}