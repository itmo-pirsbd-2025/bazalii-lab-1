# Parallel sorting

## Algorithms
I've implemented two different algorithms for parallel sorting in ascending order:
- `NaiveSorter`. It divides input array into parts, sorts each part and merges them until there is only one part left, which is the actual result. The number of parts depends on the number of cores on your machine.
- `CleverSorter`. It divides input array into parts that contain elements from ranges that depend on maximum array element. After that it sorts each part in parallel and there is only one merge needed to get the final result, because each part contains only elements from one range and ranges are disjoint parts of the input array. The number of parts depends on the number of cores on your machine.

## How to run
- To run correctness tests that check that result returned by a sorting algorithm is a correctly sorted array by running `main` method:
- To run jmh benchmarks use command: `./gradlew jmh`

## Benchmarks results
| Benchmark                      | Size    | Mode | Cnt | Score  | Error  | Units |
|-------------------------------|---------|------|-----|--------|--------|-------|
| Benchmarks.builtInSort        | 100     | avgt | 25  | 0.001  | ±0.001 | ms/op |
| Benchmarks.builtInSort        | 10000   | avgt | 25  | 0.338  | ±0.011 | ms/op |
| Benchmarks.builtInSort        | 1000000 | avgt | 25  | 57.721 | ±3.599 | ms/op |
| Benchmarks.cleverParallelSort | 100     | avgt | 25  | 0.009  | ±0.001 | ms/op |
| Benchmarks.cleverParallelSort | 10000   | avgt | 25  | 0.171  | ±0.005 | ms/op |
| Benchmarks.cleverParallelSort | 1000000 | avgt | 25  | 15.806 | ±0.542 | ms/op |
| Benchmarks.naiveParallelSort  | 100     | avgt | 25  | 0.023  | ±0.002 | ms/op |
| Benchmarks.naiveParallelSort  | 10000   | avgt | 25  | 0.308  | ±0.015 | ms/op |
| Benchmarks.naiveParallelSort  | 1000000 | avgt | 25  | 21.917 | ±1.941 | ms/op |

Both algorithms are not as effective as the default Java implementation of sorting.
Naive sort is surprisingly more effective than clever.

# Параллельная сортировка

## Алгоритмы
Реализованы два алгоритма для параллельной сортировки:
- `NaiveSorter`. Делит исходный массив на части, далее сортирует каждую из частей и "склеивает" их до тех пор пока не останется ровно одна часть, которая и будет являться результатом. Количество частей зависит от количества процессоров на машине.
- `CleverSorter`. Делит исходный массив на части, какждая из которых содержит элементы из промежутков, которые зависят от значения максимального элемента в исходно массиве. После этого сортирует каждую часть параллельно и в конце нужна ровно одна склейка для получения финального результата, потому что в каждой части находятся непересекающиеся элементы из определенного промежутка. Количество частей зависит от количества процессоров на машине.

## Как запустить
- Для запуска проверки корректности алгоритмов достаточно запустить метод `main`
- Для запуска бенчмарков jmh достаточно выполнить команду: `./gradlew jmh`

## Результаты бенчмарков
Со сводной таблицей можете ознакомиться в [секции](#benchmarks-results).
Обе реализации проигрывают стандартной сортировке в Java.
Наивный подход также оказался неожиданно более эффективным, чем "умный" вариант.