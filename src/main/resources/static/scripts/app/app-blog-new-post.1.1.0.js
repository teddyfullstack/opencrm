/*
 |--------------------------------------------------------------------------
 | Shards Dashboards: Blog Add New Post Template
 |--------------------------------------------------------------------------
 */

'use strict';

(function ($) {
  $(document).ready(function () {

    var toolbarOptions = [
      [{ 'header': [1, 2, 3, 4, 5, false] }],
      ['bold', 'italic', 'link', 'image', 'underline', 'strike'],        // toggled buttons
      ['blockquote', 'code-block'],
      [{ 'header': 1 }, { 'header': 2 }],               // custom button values
      [{ 'list': 'ordered'}, { 'list': 'bullet' }],
      [{ 'script': 'sub'}, { 'script': 'super' }],      // superscript/subscript
      [{ 'indent': '-1'}, { 'indent': '+1' }],          // outdent/indent                                       // remove formatting button
    ];

    // Init the Quill RTE
    window.quill = new Quill('.quill-editor', {
      modules: {
        toolbar: toolbarOptions
      },
      placeholder: 'Words can be like x-rays if you use them properly...',
      theme: 'snow'
    });

    $("form.quill-parents").on("submit",function() {
      var content = quill.container.firstElementChild;
      var html = content.innerHTML;
      var text = content.innerText;
      if (text.trim()) {
        $("<input />").attr("type", "hidden")
            .attr("name", $('.quill-editor').attr('name'))
            .attr("value", html)
            .appendTo(this);
      }
    });
  });
})(jQuery);
