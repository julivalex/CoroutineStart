# CoroutineStart
1. Каждая корутина должна быть запущена внутри какого-то скупа с определенным жизненным циклом 
2. Все корутины запускаются в виде иерархии объектов джобс
3. Пока дочерние джобы не завершат свою работу родительская джоба будет активна и она так же не будет закрыта 
4. Если родительская джоба отменяется, то отменяются и все дочерние корутины
5. Если в какой-то из джобы произошло исключение, то это исключение передается вверх по иерархии. И если родительская корутина не умеет обрабатывать исключение, то будет краш. Если произошла ошибка, то все корутины в рамках этого скоупа будут отменены

CoroutineScope -> interface CoroutineContext -> Dispatchers, Job, ExceptionHandler, CoroutineName
