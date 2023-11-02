import {useEffect, useRef, useState} from "react";
import {MinusPlus} from "../../MinusPlus";

type ProductSearchProps = {
    products: string[];
    saveProduct: (index: number, product: string | null, quantity: number, grammar: string) => void;
    removeProduct: (index: number) => void;
    index: number;
    grammarNames: string[];
    width?: string;
    productVal?: string;
    quantityVal?: number;
    grammarVal?: string
}
export const ProductForm = ({
                                products,
                                grammarNames,
                                saveProduct,
                                removeProduct,
                                index,
                                width,
                                productVal,
                                quantityVal,
                                grammarVal,
                            }: ProductSearchProps) => {
    const [product, setProduct] = useState<string>(productVal ?? '');
    const [quantity, setQuantity] = useState<number>(quantityVal ?? 0);
    const [results, setResults] = useState<string[]>([]);
    const [save, setSave] = useState<boolean>(false);
    const [grammar, setGrammar] = useState<string>(grammarVal ?? 'L');
    const searchBoxRef = useRef<HTMLUListElement>(null);

    useEffect(() => {
        const handleOutsideClick = (event: MouseEvent) => {
            if (searchBoxRef.current && !searchBoxRef.current.contains(event.target as Node)) {
                setResults([]);
            }
        };

        document.addEventListener('mousedown', handleOutsideClick);

        return () => {
            document.removeEventListener('mousedown', handleOutsideClick);
        };
    }, []);

    const onChangeHandler = async (event: any) => {
        const search = event.target.value.toLowerCase();
        setProduct(search);
        if (search.length === '') {
            return setResults([]);
        }

        const results: string[] = products.filter((product) => {
            return product.toLowerCase().includes(search);
        });

        setResults(results);
        saveProduct(index, search, quantity, grammar);
    }

    const onChangeQuantityHandler = (event: any) => {
        const quantity = event.target.value;
        setQuantity(quantity);
        saveProduct(index, product, quantity, grammar);
    }

    const onChangeGrammarHandler = (event: any) => {
        const grammar = event.target.value;
        setGrammar(grammar);
        saveProduct(index, product, quantity, grammar);
    }

    const onClickSearchedProduct = (product: string) => {
        setProduct(product)
        setResults([]);
    }

    return (
        <div className={`relative flex flex-row gap-2 mr-4`}>
            <input
                name={'product'}
                className={`py-2.5 px-0  bg-transparent w-3/5 border-0 border-b-2 appearance-none text-gray-400 disabled:text-amber-50 border-gray-700 focus:outline-none focus:ring-0 focus:border-gray-200 peer`}
                type='text'
                placeholder={'Product'}
                value={product}
                onChange={onChangeHandler}
                disabled={save}
            />
            <ul ref={searchBoxRef}
                className={"absolute mt-12 bg-gray-800 h-max-32 overflow-y-auto  w-3/5 rounded-b-md  " + (results.length > 0 ? 'block' : 'hidden')}>
                {results ? results.map((value, index) => {
                    return <li
                        className={"text-white hover:bg-gray-600"}
                        onClick={() => onClickSearchedProduct(value)}
                        key={index}>{value}
                    </li>
                }) : null}
            </ul>
            <input
                name={'quantity'}
                className={`py-2.5 px-0  bg-transparent w-1/6 border-0 border-b-2 appearance-none text-gray-400 disabled:text-amber-50 border-gray-700 focus:outline-none focus:ring-0 focus:border-gray-200 peer`}
                placeholder={'Quantity'}
                type='number'
                value={quantity}
                disabled={save}
                onChange={onChangeQuantityHandler}
            />
            <select
                name={'grammar'}
                value={grammar}
                onChange={onChangeGrammarHandler}
                className={`py-2.5 px-0  bg-transparent w-1/6 border-0 border-b-2 appearance-none text-gray-400 border-gray-700  disabled:text-amber-50 focus:outline-none focus:ring-0 focus:border-gray-200 peer`}
                disabled={save}
            >{grammarNames.map((grammarName, i) => {
                return (
                    <option key={i} value={grammarName}>{grammarName}</option>
                )
            })}
            </select>

            <MinusPlus
                onMinusClick={() => removeProduct(index)}
                position={'horizontal'}
            />

        </div>

    )
}