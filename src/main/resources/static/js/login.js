$(function () {
    const $loginForm = $('#loginForm');
    //$변수 제이쿼리 변수 $('css선택')
    if ($loginForm.length) {

        $loginForm.validate({

            rules: {
                email: {
                    required: true,
                    email: true
                },
                password: {
                    required: true
                }
            },
            messages: {
                email: {
                    required: '이메일을 입력해 주세요',
                    email: '이메일 형식에 맞게 입력해 주세요'
                },
                password: {
                    required: '패스워드 입력해 주세요'
                }
            }
            ,errorElement: 'span',
            errorPlacement: function (error, element) {
                error.addClass('help-block');
                error.insertAfter(element);
            }
        });
    }
})